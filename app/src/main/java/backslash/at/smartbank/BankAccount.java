package backslash.at.smartbank;

import java.util.Date;

public class BankAccount {
        public int id;

        public String iban;
        public int balance;
        public Date created;
        public Date updated;

        @Override
        public String toString() {
                return this.iban;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                id = id;
        }

        public String getIban() {
                return iban;
        }

        public void setIban(String iban) {
                iban = iban;
        }

        public int getBalance() {
                return balance;
        }

        public void setBalance(int balance) {
                balance = balance;
        }

        public Date getCreated() {
                return created;
        }

        public void setCreated(Date created) {
                created = created;
        }

        public Date getUpdated() {
                return updated;
        }

        public void setUpdated(Date updated) {
                updated = updated;
        }
}
