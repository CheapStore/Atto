package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.dto.TerminalDTO;
import org.example.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TerminalReporistory {


    public static List<TerminalDTO> getTerminal() {
        List<TerminalDTO> terminalList = new LinkedList<>();
        try {
            Connection connection = org.example.db.DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "select * from terminal";

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                TerminalDTO terminal=new TerminalDTO();
                terminal.setCode(resultSet.getString("code"));
                terminal.setAddress(resultSet.getString("address"));
                terminal.setStatus(Status.valueOf(resultSet.getString("status")));
                terminal.setCreated_date(resultSet.getDate("created_date").toLocalDate());
                terminalList.add(terminal);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terminalList;
    }

    public boolean creatTerminal(TerminalDTO terminal) {
        int res=0;
            try {
                Connection connection = org.example.db.DatabaseUtil.getConnection();
                String sql = "insert into terminal(code,address) values (?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, terminal.getCode());
                preparedStatement.setString(2, terminal.getAddress());
                res = preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res!=0;
        }


    public boolean chesk(String code) {
        boolean execute = false;
        try {
            Connection connection = org.example.db.DatabaseUtil.getConnection();
            String sql = "select  chesk(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"'"+code+"'");
            execute = preparedStatement.execute();
            System.out.println("execute = " + execute);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return execute;

    }

    public boolean updateterminal(TerminalDTO terminladto) {
        int res=0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update terminal set code=?,address=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, terminladto.getCode());
            preparedStatement.setString(2,terminladto.getAddress());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res!=0;


    }

    public boolean delete(String code1) {
        int res=0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "DELETE FROM terminal WHERE code =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,code1);
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res!=0;


    }
}

