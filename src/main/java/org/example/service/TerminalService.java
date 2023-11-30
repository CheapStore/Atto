package org.example.service;

import org.example.dto.TerminalDTO;
import org.example.repository.TerminalReporistory;

import java.util.List;

public class TerminalService {
    TerminalReporistory terminalReporistory=new TerminalReporistory();
    public boolean creatTerminal(TerminalDTO terminal) {
        List<TerminalDTO> terminalList = getTerminal();
            if (terminalList!=null) {
                for (TerminalDTO terminaldto : terminalList) {
                    if (terminaldto.getCode().equals(terminal.getCode())) {
                        System.out.println("Terminal is available !!!");
                        return false;
                    }
                }
            }
            boolean result = terminalReporistory.creatTerminal(terminal);
            if (result) {
                System.out.println("terminal created successfuly 👌👌👌");
            }else {
                System.out.println("An error occurred while creating the terminal !!!");
            }
            return result;
        }


        public   List<TerminalDTO> getTerminal() {
            List<TerminalDTO> terminallist = TerminalReporistory.getTerminal();
            return terminallist;
        }

    public boolean chesk(String code) {
       return terminalReporistory.chesk(code);
    }

    public void updateterminal(TerminalDTO terminladto) {
        boolean updateterminal = terminalReporistory.updateterminal(terminladto);
        if (updateterminal){
            System.out.println("terminal update successfuly 👌👌👌");
        }else {
            System.out.println("An error occurred while updating the terminal !!!");
        }

    }

    public void delete(String code) {
        boolean delete = terminalReporistory.delete(code);
        if (delete){
            System.out.println("terminal row  delete  successfuly 👌👌👌");
        }else {
            System.out.println("An error occurred while deleting row the terminal !!!");
        }
    }
}

