package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentRequest;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentResponse;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentStatusResult;
import br.com.fiap.techchallenge.infra.web.dto.external_payment.ExternalPaymentApiRequest;
import br.com.fiap.techchallenge.infra.web.dto.external_payment.ExternalPaymentApiResponse;
import br.com.fiap.techchallenge.infra.web.dto.external_payment.ExternalPaymentApiStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExternalPaymentGatewayAdapter implements ExternalPaymentGatewayPort {

    private final RestClient restClient;

    public ExternalPaymentGatewayAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${external.payment.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public ExternalPaymentResponse submitPayment(ExternalPaymentRequest request) {

        // The external payment processor accepts only integer amounts.
        // To preserve monetary precision in the domain model, the application keeps BigDecimal internally
        // and converts the amount only at the integration boundary.
        ExternalPaymentApiRequest apiRequest = new ExternalPaymentApiRequest(
                request.amount().setScale(0, java.math.RoundingMode.HALF_UP).intValue(),
                request.paymentId(),
                request.clientId()
        );

        ExternalPaymentApiResponse response = restClient.post()
                .uri("/requisicao")
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiRequest)
                .retrieve()
                .body(ExternalPaymentApiResponse.class);

        String rawStatus = response != null ? response.status() : null;
        boolean accepted = "accepted".equalsIgnoreCase(rawStatus);

        return new ExternalPaymentResponse(accepted, rawStatus);
    }

    @Override
    public ExternalPaymentStatusResult getPaymentStatus(String paymentId) {

        ExternalPaymentApiStatusResponse response = restClient.get()
                .uri("/requisicao/{pagamento_id}", paymentId)
                .retrieve()
                .body(ExternalPaymentApiStatusResponse.class);

        return new ExternalPaymentStatusResult(
                response != null ? response.pagamento_id() : paymentId,
                response != null ? response.status() : null
        );
    }
}
