package br.com.websocket.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author statelessbr https://github.com/statelessbr
 */
@ServerEndpoint("/control")
public class Controller {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session sessao) {
        peers.add(sessao);
    }

    @OnClose
    public void onClose(Session sessao) {
        peers.remove(sessao);
    }

    @OnMessage
    public void onMessage(String mensagem, Session sessao) throws IOException {
        String resposta;
        resposta = ("Data " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "<br>");
        resposta = resposta.concat(mensagem);

        if (resposta.contains("selectAllSessions")) {

            resposta = resposta.replace("selectAllSessions", "");

            resposta = resposta.concat(selectAllSessions());
        }

        sessao.getBasicRemote().sendText(resposta + " <br>Enviado de: " + sessao.getId());

    }

    public String selectAllSessions() {
        String id = "Peers conectados: <br>";

        // nao substituir por lambda, serios problemas vao acontecer
        for (Session ativo : peers) {
            id += ativo.getId() + " - ativo: " + ativo.isOpen() + "<br/>";
        }
        return id;
    }

}
