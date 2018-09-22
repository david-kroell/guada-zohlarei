package backslash.at.smartbank;

import java.util.List;

public interface IVolleyCallbackBills {
    void getAllBills(List<Bill> bills);
    void uploadBillSuccess(Bill b);
    void billError(String error);

}
