package templates.security;

import com.cashflow.auth.core.domain.authentication.CashFlowAuthentication;
import com.cashflow.auth.core.domain.enums.RoleEnum;

import java.util.List;

public class AuthenticationTemplates {

    private AuthenticationTemplates() {}

    public static CashFlowAuthentication cashFlowAuthentication() {
        return new CashFlowAuthentication(
                1L,
                "Vinicius",
                "Peralta",
                "vinicius-peralta@dev.com",
                List.of(RoleEnum.CASH_FLOW_BASICS),
                true,
                "VALID.JWT.TOKEN"
        );
    }
}
