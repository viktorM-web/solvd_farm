package com.solvd.farm.service.menu.admin;

import com.solvd.farm.domain.User;
import com.solvd.farm.domain.enums.Role;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.DocumentReader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Optional;

@Slf4j
public class CreatingUserMenu implements IMenu {

    @Setter
    private Session session;

    @Override
    public void execute() {
        boolean exit = false;
        while (!exit) {
            log.info("you want to create user \n by xml press [1]\n by other way press[2] \n if you want exit [0]");
            String requestForMenu = session.getRequestForMenu();
            switch (requestForMenu) {
                case "0" -> {
                    exit = true;
                    log.info("back to user menu");
                }
                case "2" -> {
                    exit = true;
                    log.info("no implementation back to user menu");
                }
                case "1" -> {
                    Document doc = null;
                    while (doc == null) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Document> document = DocumentReader.getDocument(requestForMenu);
                        if (document.isPresent()) {
                            doc = document.get();
                        }
                    }
                    NodeList nodeList = doc.getChildNodes();
                    Node item = nodeList.item(0);
                    NodeList childNodes = item.getChildNodes();

                    String login = childNodes.item(1).getTextContent();
                    String password = childNodes.item(3).getTextContent();
                    String nameRole = childNodes.item(5).getTextContent();

                    User user = new User(null, login, password, Role.valueOf(nameRole));
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        log.info(user + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                default -> log.info("not correct data");
            }
        }
    }
}
