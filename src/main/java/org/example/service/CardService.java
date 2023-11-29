package org.example.service;

import org.example.dto.CardDTO;
import org.example.repository.CardRepository;

import java.util.List;

public class CardService {
    CardRepository cardRepository=new CardRepository();

    public boolean createCard(CardDTO card) {
        List<CardDTO> cardList = getCardList();
        if (cardList!=null) {
            for (CardDTO cardDTO : cardList) {
                if (cardDTO.getNumber().equals(card.getNumber())) {
                    System.out.println("Card is available !!!");
                    return false;
                }
            }
        }
        boolean result = cardRepository.createCard(card);
        if (result) {
            System.out.println("Card created successfuly 👌👌👌");
        }else {
            System.out.println("An error occurred while creating the card !!!");
        }
        return result;
    }

    public   List<CardDTO> getCardList() {
        List<CardDTO> cardList =cardRepository.getCardList();
        return cardList;
    }

    public void updateCard(CardDTO cardDTO,String number) {
        boolean update = cardRepository.update(cardDTO, number);
        if (update){
            System.out.println("Cart updates successfuly \uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C\"");
        }else {
            System.out.println("Not fount");
        }

    }

    public boolean chesk(String number) {
      return cardRepository.chesk(number);

    }
}
