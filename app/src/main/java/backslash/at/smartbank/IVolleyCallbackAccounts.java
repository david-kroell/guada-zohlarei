package backslash.at.smartbank;

import java.util.List;

public interface IVolleyCallbackAccounts {
    void getAllAccounts(List<BankAccount> list);
    void problemOccured(String errorMessage);
}
