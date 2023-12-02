package org.example.controller;

import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.dto.TerminalDTO;
import org.example.enums.ProfileRole;
import org.example.enums.Status;
import org.example.service.CardService;
import org.example.service.TerminalService;
import org.example.service.UserService;
import org.example.utils.ScannerUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Controller {
    static ScannerUtils scanner = new ScannerUtils();
    static UserService userService = new UserService();
    static TerminalService terminalService=new TerminalService();
    CardService cardService = new CardService();


    public void start() {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        databaseUtil.createProfileTable();
        databaseUtil.createCardTable();
        databaseUtil.createTerminalTable();
        databaseUtil.createTransactionTable();
        do {
            showMain();
            int action = getAction();
            switch (action) {
                case 1 -> {
                    login();
                }
                case 2 -> {
                    registration();
                }
            }
        } while (true);

    }

    private void registration() {
        String name = scanner.nextLine("Enter name:");
        String surname = scanner.nextLine("Enter surname:");
        String phone;
        String password;
        do {
            phone = scanner.nextLine("Enter phoneNumber: ");
            password = scanner.nextLine("Enter password: ");
        } while (phone == null || password == null);
        ProfileDTO profile = new ProfileDTO();
        profile.setName(name);
        profile.setSurname(surname);
        profile.setPhone(phone);
        profile.setPassword(password);
        profile.setProfileRole(ProfileRole.USER);
        boolean result = userService.registration(profile);
        if (result) {
            System.out.println("Successful 👌👌👌");
            userMenu(profile);
        } else {
            System.out.println("Error registration!!!");
        }

    }

    private void login() {
        String phoneNumber = null;
        String password = null;
        do {
            phoneNumber = scanner.nextLine("Enter phoneNumber: ");
            password = scanner.nextLine("Enter password: ");
        } while (phoneNumber == null || password == null);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setPhone(phoneNumber);
        profileDTO.setPassword(password);

        ProfileDTO profile = userService.login(profileDTO);
        if (profile == null) {
            System.out.println("Not found");
            return;
        } else {
            if (profile.getStatus().equals(Status.NO_ACTIVE)) {
                System.out.println("Not found");
                return;
            }
            if (profile.getProfileRole().equals(ProfileRole.USER)) {
                userMenu(profile);
            } else {
                adminMenu(profile);
            }
        }


    }

    private void adminMenu(ProfileDTO profile) {
        while (true) {
            System.out.println("""
                    ADMIN MENU
                    1.Card
                    2.Terminal
                    3.Profile
                    4.Transaction
                    5.Statistic
                    0.Exit
                    """);
            int option = scanner.nextInt("Choose option: ");
            switch (option) {
                case 1 -> cardMenu(profile);
                case 2 -> terminalMenu(profile);
                case 3 -> profile_menu(profile);
                case 4 ->{

                }
                 case 5 -> {
                }
                case 0-> {
                    return;
                }
                default -> System.out.println("Wrong");
            }
        }
//        (Card)
//        1. Create Card(number,exp_date)
//        2. Card List
//        3. Update Card (number,exp_date)
//        4. Change Card status
//        5. Delete Card
//
//        (Terminal)
//        6. Create Terminal (code unique,address)
//        7. Terminal List
//        8. Update Terminal (code,address)
//        9. Change Terminal Status
//        10. Delete
//
//                (Profile)
//        11. Profile List
//        12. Change Profile Status (by phone)
//
//        (Transaction)
//                13. Transaction List
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        14. Company Card Balance
//                (card bo'ladi shu cardga to'lovlar tushadi. bu sql da insert qilinga bo'ladi)
//
//        (Statistic)
//                15. Bugungi to'lovlar
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        16. Kunlik to'lovlar (bir kunlik to'lovlar):
//        Enter Date: yyyy-MM-dd
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        17. Oraliq to'lovlar:
//        Enter FromDate: yyyy-MM-dd
//        Enter ToDate:   yyyy-MM-dd
//        18. Umumiy balance (company card dagi pulchalar)
//        19. Transaction by Terminal:
//        Enter terminal number:
//        20. Transaction By Card:
//        Enter Card number:
    }

    private void profile_menu(ProfileDTO profile) {
        while (true) {
            System.out.println("""
                      1. Profile List
                      2. Change Profile Status (by phone)
                      0.Exit
                    """);
            int option = scanner.nextInt("Choose option: ");

            switch (option) {
                case 1 -> {
                    List<ProfileDTO> profil_list = userService.getProfillist();
                    if (profil_list != null) {
                        for (ProfileDTO cardDTO : profil_list) {
                            System.out.println(cardDTO);
                        }
                    }
                }
                case 2 -> {
                    updateProfile(profile);

                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Drong");


            }
        }
    }

    private void updateProfile(ProfileDTO profile) {
        String old,neww;
        do {
            old = scanner.nextLine("Enter old phone number :");
            neww = scanner.nextLine("Enter neww  phone number :");
        }while (old.isEmpty()||neww.isEmpty());
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setPhone(neww);
        userService.updateProfile(profileDTO,old);
    }

    private void terminalMenu(ProfileDTO profile) {
        while (true) {
            System.out.println("""
                    1. Create Terminal (code unique,address)
                    2. Terminal List
                    3. Update Terminal (code,address)
                    4. Change Terminal Status
                    5. Delete
                    0.Exit
                    """);
            int option = scanner.nextInt("Choose option: ");

            switch (option) {
                case 1 -> creatterminal(profile);
                case 2 -> {
                    List<TerminalDTO> terminalList = terminalService.getTerminalList();
                    if (terminalList != null) {
                        for (TerminalDTO terminalDTO : terminalList) {
                            System.out.println(terminalDTO);
                        }
                    }
                }
                case 3 -> updateterminal(profile);
                case 4 -> {
                    changestatusTerminal(profile);
                }
                case 5 -> {
                }
                case 0-> {
                    return;
                }
                default -> {
                    System.out.println("Wrong");
                }
            }


        }
    }

    private void changestatusTerminal(ProfileDTO profile) {
        String code;
        do {
            code = scanner.nextLine(" enter terminal code:");
        }while (code.isEmpty());
        terminalService.update_status_active_terminal(code);
    }

    private void updateterminal(ProfileDTO profile) {
        String old,neww,newwAdres;
        do {
            old = scanner.nextLine("old enter terminal code:");
            neww= scanner.nextLine("new enter terminal code:");
            newwAdres=scanner.nextLine("new enter terminal adress:");
        }while (old.isEmpty()||neww.isEmpty());
        TerminalDTO terminalDTO=new TerminalDTO();
        terminalDTO.setAddress(newwAdres);
        terminalDTO.setCode(neww);
        terminalService.updateterminal(terminalDTO,old);

    }

    private void cardMenu(ProfileDTO profile) {
        System.out.println("""
                1. Create Card(number,exp_date)
                2. Card List
                3. Update Card (number,exp_date)
                4. Change Card status
                5. Delete Card""");
        int option = scanner.nextInt("Choose option: ");

        switch (option) {
            case 1 -> createCard(profile);
            case 2 -> {
                List<CardDTO> cardList = cardService.getCardList();
                if (cardList != null) {
                    for (CardDTO cardDTO : cardList) {
                        System.out.println(cardDTO);
                    }
                }
            }
            case 3 -> updatecard(profile);
            case 4 -> update_status_active(profile);
            case 5 -> deletea_card(profile);

            default -> {
                System.out.println("Wrong");
            }
        }

    }

    private void deletea_card(ProfileDTO profile) {
        String number;
        do {
            number = scanner.nextLine("enter the current card number :");
        } while (number.trim().isEmpty());

        cardService.delete_card(number);

    }

    private void update_status_active(ProfileDTO profile) {
        String number;
        int year;
        do {
            number = scanner.nextLine("enter the current card number :");
        } while (number.trim().isEmpty());
        Boolean b = cardService.chesk(number);
        if (b) {
cardService.updateStatus(number);
        }
    }

    private void updatecard(ProfileDTO profile) {
        String number,numbernew;
        int year;
        do {
            number = scanner.nextLine("enter the current card number :");
        }while (number.trim().length()==0);
        Boolean b = cardService.chesk(number);
        if (b){
            do {
                numbernew = scanner.nextLine("enter a new card number :");
                year = scanner.nextInt("Enter the expiration date (3-10): ");
            }while (year==0);
            CardDTO card = new CardDTO();
            card.setNumber(numbernew);
            card.setExp_date(LocalDate.now().plusYears(year));
            cardService.updateCard(card);
        }else {
            System.out.println("Qayta urining");
        }

    }
    private void profilee(){
        userService.profilee();
    }
       private void creatterminal(ProfileDTO profileDTO){
           String code,adress;

           do {
               code = scanner.nextLine("Enter terminal code: ");
               adress = scanner.nextLine("Adress:");
           } while (code.trim().length() == 0);
           TerminalDTO terminaldto = new TerminalDTO();
           terminaldto.setCode(code);
           terminaldto.setAddress(adress);
           terminalService.creatTerminal(terminaldto);

       }
    private void createCard(ProfileDTO profile) {
        String cardNumber;
        int year;
        do {
            cardNumber = scanner.nextLine("Enter Card number: ");
            year = scanner.nextInt("Enter the expiration date (3-10): ");
        } while (cardNumber.trim().length() == 0 || year == 0);
        CardDTO card = new CardDTO();
        card.setNumber(cardNumber);
        card.setExp_date(LocalDate.now().plusYears(year));
        card.setPhone(profile.getPhone());
        cardService.createCard(card);

    }

    private void userMenu(ProfileDTO profile) {
//        1. Add Card (number) - (cartani profile ga ulab qo'yamiz.) (added_date)
//        Enter Card Number:
//        (kiritilgan number database da bo'lishi kerak.)
//        2. Card List (number,exp_date,balance)
//        3. Card Change Status
//        4. Delete Card (visible_user = false,deleted_user)
//
//        4. ReFill (pul tashlash) (carta userga tegishli bo'lishi kerak.)
//        Enter card number:
//        Balance:
//        (Transaction with type 'ReFill')
//
//        (Transaction)
//                5. Transaction
//        CardNumber, Address, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        6. Make Payment (pul to'lash)
//        Enter cardNumber:
//        Enter terminal number:
//        (Transaction with type 'Payment')
               while (true) {
            System.out.println("""
                    USER MENU
                   1.My Card
                   2.ReFill.
                   3.Transactions
                   4.Make Payment
                   0.Exit
                    """);
    int option = scanner.nextInt("Choose option: ");

    switch (option) {
        case 1 -> mycardmenu(profile);
//                addcard(profile);
        case 2 -> {
            List<CardDTO> cardList = cardService.getCardList();
            if (cardList != null) {
                for (CardDTO cardDTO : cardList) {
                    System.out.println(cardDTO);
                }
            }
        }
        case 3 -> {
        }
        case 4 -> {
        }
        case 5 -> {
        }
        case 0-> {
            return;
        }
        default -> System.out.println("Wrong");
    }

}

    }
    private void mycardmenu(ProfileDTO profile){
        while (true) {
            System.out.println("""
                    1.Add Card.
                    2.List of Card.
                    3.Change Card Status.
                    4.Delete Card.
                    5.Update Card.
                    0.Exit."
                    """);
            int i = scanner.nextInt("Choose option: ");
            switch (i){
                case 1->addcard(profile);
                case 2->cardService.getCardList();
                case 0-> {
                    return;
                }
                default -> System.out.println("Wrong:");
            }
        }


    }

    private void addcard(ProfileDTO profile){
        String newCardNumber;
        int year;
        do {
            newCardNumber = scanner.nextLine("Enter  Card number: ");
             year = scanner.nextInt("enter year:");
        } while (newCardNumber.trim().length() == 0);
        CardDTO card = new CardDTO();
        card.setNumber(newCardNumber);
        card.setExp_date(LocalDate.now().plusYears(year));
        card.setPhone(profile.getPhone());
        cardService.createCard(card);
    }

    private int getAction() {
        int option = scanner.nextInt("Choose action: ");
        return option;
    }

    private void showMain() {
        System.out.println("""
                1. Login
                2. Registration:""");
    }


}
