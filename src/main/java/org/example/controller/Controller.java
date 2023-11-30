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
import java.util.List;
import java.util.Objects;

public class Controller {
    static ScannerUtils scanner = new ScannerUtils();
    static UserService userService = new UserService();
    CardService cardService = new CardService();
    TerminalService terminalService=new TerminalService();


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
        System.out.println("""
                1.Card
                2.Terminal
                3.Profile
                4.Transaction
                5.Statistic""");
        int option = scanner.nextInt("Choose option: ");
        switch (option) {
            case 1 -> {
                cardMenu(profile);
            }
            case 2 -> {
                terminalMenu(profile);
            }
            case 3 -> {
            }
            case 4 -> {
            }
            case 5 -> {
            }
            default -> {
                System.out.println("Wrong");
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
//                6. Create Terminal (code unique,address)
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

    private void terminalMenu(ProfileDTO profile) {
        System.out.println("""
                1. Create Terminal (code unique,address)
                2. Terminal List
                3. Update Terminal (code,address)
                4. Change Terminal Status
                5. Delete
                """);
        int option = scanner.nextInt("Choose option: ");
        switch (option) {
            case 1 -> {
                createterminal();
            }
            case 2 -> {
                List<TerminalDTO> terminalist = terminalService.getTerminal();
                if (terminalist != null) {
                    for (TerminalDTO dto : terminalist) {
                        System.out.println(dto);
                    }
                }
            }
            case 3 -> {
            updateTerminal(profile);
            }
            case 4 -> {
            }
            case 5 -> {
                delete_terminal(profile);;
            }
            default -> {
                System.out.println("Wrong");
            }
        }
    }

    private void delete_terminal(ProfileDTO profile) {
        String code;

        do {
            code = scanner.nextLine("enter the current terminal code :");
        }while (code.trim().length()==0);
        TerminalDTO terminladto = new TerminalDTO();
        boolean b = terminalService.chesk(code);
        if (b){
            terminalService.delete(code);
        }
    }

    private void updateTerminal(ProfileDTO profile) {
        String code,newcode,adresnew;

        do {
            code = scanner.nextLine("enter the current terminal code :");
        }while (code.trim().length()==0);
        TerminalDTO terminladto = new TerminalDTO();
        boolean b = terminalService.chesk(code);
        if (b){
            do {
                newcode = scanner.nextLine("enter a new terminal code :");
                adresnew = scanner.nextLine("enter a new terminal addrss ");
            }while (Objects.equals(code, newcode));
            terminladto.setCode(newcode);
            terminladto.setAddress(adresnew);
            terminalService.updateterminal(terminladto);
        }else {
            System.out.println("Qayta urining");
        }

    }

    private void createterminal() {
        String address,code;
           address = scanner.nextLine("Enter terminal address: ");
           code = scanner.nextLine("Enter terminal code ");
        TerminalDTO terminal=new TerminalDTO();
        terminal.setAddress(address);
        terminal.setCode(code);
        terminalService.creatTerminal(terminal);
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
            case 1 -> {
                createCard(profile);
            }
            case 2 -> {
                List<CardDTO> cardList = cardService.getCardList();
                if (cardList != null) {
                    for (CardDTO cardDTO : cardList) {
                        System.out.println(cardDTO);
                    }
                }
            }
            case 3 -> {
                updatecard(profile);
            }
            case 4 -> {
            }
            case 5 -> {
            }
            default -> {
                System.out.println("Wrong");
            }
        }

    }

    private void updatecard(ProfileDTO profile) {
        String number,numbernew;
        int year;
        do {
            number = scanner.nextLine("enter the current card number :");
        }while (number.trim().length()==0);
        CardDTO card = new CardDTO();
        boolean b = cardService.chesk(number);
        if (b){
            do {
                numbernew = scanner.nextLine("enter a new card number :");
                year = scanner.nextInt("Enter the expiration date (3-10): ");
            }while (Objects.equals(number, numbernew)||year==0);
            card.setNumber(numbernew);
            card.setExp_date(LocalDate.now().plusYears(year));
            card.setPhone(profile.getPhone());
            cardService.updateCard(card, number);
        }else {
            System.out.println("Qayta urining");
        }

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

        System.out.println("""
                1. Add Card (number) - (cartani profile ga ulab qo'yamiz.) (added_date)
                //        Enter Card Number:""");


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
