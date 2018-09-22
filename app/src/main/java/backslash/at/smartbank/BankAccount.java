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

        public int getId() {
                return Id;
        }

        public void setId(int id) {
                Id = id;
        }

        public String getIban() {
                return Iban;
        }

        public void setIban(String iban) {
                Iban = iban;
        }

        public int getBalance() {
                return Balance;
        }

        public void setBalance(int balance) {
                Balance = balance;
        }

        public Date getCreated() {
                return Created;
        }

        public void setCreated(Date created) {
                Created = created;
        }

        public Date getUpdated() {
                return Updated;
        }

        public void setUpdated(Date updated) {
                Updated = updated;
        }
}
