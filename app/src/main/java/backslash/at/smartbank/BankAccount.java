package backslash.at.smartbank;

import java.util.Date;

public class BankAccount {
        public int Id;

        public String Iban;
        public int Balance;
        public Date Created;
        public Date Updated;

        @Override
        public String toString() {
                return this.Iban;
        }
}
