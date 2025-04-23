package br.ufpb.dcx.amigosecreto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaAmigoMap {

    private Map<String, Amigo> amigos;
    private List<Mensagem> mensagens;

    public SistemaAmigoMap() {
        this.amigos = new HashMap<>();
        this.mensagens = new ArrayList<>();
    }

    public void cadastraAmigo(String nomeAmigo, String emailAmigo) throws AmigoJaExisteException {
        if (amigos.containsKey(emailAmigo)) {
            throw new AmigoJaExisteException("Amigo com esse email já existe.");
        }
        this.amigos.put(emailAmigo, new Amigo(nomeAmigo, emailAmigo));
    }

    public Amigo pesquisaAmigo(String emailAmigo) throws AmigoInexistenteException {
        if (!amigos.containsKey(emailAmigo)) {
            throw new AmigoInexistenteException("Amigo não encontrado.");
        }
        return amigos.get(emailAmigo);
    }

    public void enviarMensagemParaTodos(String texto, String emailRemetente, boolean ehAnonima) {
        MensagemParaTodos mensagem = new MensagemParaTodos(texto, emailRemetente, ehAnonima);
        this.mensagens.add(mensagem);
    }

    public void enviarMensagemParaAlguem(String texto, String emailRemetente, String emailDestinatario, boolean ehAnonima) {
        MensagemParaAlguem mensagem = new MensagemParaAlguem(texto, emailRemetente, ehAnonima, emailDestinatario);
        this.mensagens.add(mensagem);
    }

    public List<Mensagem> pesquisaMensagensAnonimas() {
        List<Mensagem> mensagensAnonimas = new ArrayList<>();
        for (Mensagem m : this.mensagens) {
            if (m.isAnonima()) {
                mensagensAnonimas.add(m);
            }
        }
        return mensagensAnonimas;
    }

    public List<Mensagem> pesquisaTodasAsMensagens() {
        return new ArrayList<>(this.mensagens);
    }

    public void configuraAmigoSecretoDe(String emailDaPessoa, String emailAmigoSorteado) throws AmigoInexistenteException {
        Amigo amigo = pesquisaAmigo(emailDaPessoa);
        amigo.setEmailAmigoSorteado(emailAmigoSorteado);
    }

    public String pesquisaAmigoSecretoDe(String emailDaPessoa) throws AmigoInexistenteException, AmigoNaoSorteadoException {
        Amigo amigo = pesquisaAmigo(emailDaPessoa);
        if (amigo.getEmailAmigoSorteado() == null) {
            throw new AmigoNaoSorteadoException("Amigo secreto ainda não foi sorteado.");
        }
        return amigo.getEmailAmigoSorteado();
    }
}