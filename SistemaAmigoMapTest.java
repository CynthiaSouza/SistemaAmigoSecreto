package br.ufpb.dcx.amigosecreto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SistemaAmigoMapTest {

    SistemaAmigoMap sistema;

    @BeforeEach
    void setUp() {
        this.sistema = new SistemaAmigoMap();
    }

    @Test
    void testSistemaAmigoMap() {
        assertTrue(sistema.pesquisaTodasAsMensagens().isEmpty());
        assertThrows(AmigoInexistenteException.class,
                () -> sistema.pesquisaAmigo("ayla@teste.com"));
    }

    @Test
    void testPesquisaECadastraAmigo() {
        try {
            sistema.pesquisaAmigo("ayla@teste.com");
            fail("Deveria falhar pois não existe ainda");
        } catch (AmigoInexistenteException e) {
            // Ok
        }
        try {
            sistema.cadastraAmigo("ayla", "ayla@teste.com");
            Amigo a = sistema.pesquisaAmigo("ayla@teste.com");
            assertEquals("ayla", a.getNome());
            assertEquals("ayla@teste.com", a.getEmail());
        } catch (AmigoJaExisteException | AmigoInexistenteException e) {
            fail("Não deveria lançar exceção");
        }
    }

    @Test
    void testEnviarMensagemParaTodos() {
        assertTrue(sistema.pesquisaTodasAsMensagens().isEmpty());
        sistema.enviarMensagemParaTodos("texto", "ayla@dcx.ufpb.br", true);
        List<Mensagem> mensagensAchadas = sistema.pesquisaTodasAsMensagens();
        assertEquals(1, mensagensAchadas.size());
        assertEquals("ayla@dcx.ufpb.br", mensagensAchadas.get(0).getEmailRemetente());
    }

    @Test
    void testEnviarMensagemParaAlguem() {
        assertTrue(sistema.pesquisaTodasAsMensagens().isEmpty());
        sistema.enviarMensagemParaAlguem("texto", "ayla@dcx.ufpb.br", "rodrigo@dcx.ufpb.br", true);
        List<Mensagem> mensagensAchadas = sistema.pesquisaTodasAsMensagens();
        assertEquals(1, mensagensAchadas.size());
        assertTrue(mensagensAchadas.get(0) instanceof MensagemParaAlguem);
        assertEquals("texto", mensagensAchadas.get(0).getTexto());
    }

    @Test
    void testPesquisaMensagensAnonimas() {
        assertTrue(sistema.pesquisaTodasAsMensagens().isEmpty());
        sistema.enviarMensagemParaAlguem("texto 1", "ayla@dcx.ufpb.br", "rodrigo@dcx.ufpb.br", false);
        assertTrue(sistema.pesquisaMensagensAnonimas().isEmpty());
        sistema.enviarMensagemParaAlguem("texto 2", "ayla@dcx.ufpb.br", "rodrigo@dcx.ufpb.br", true);
        assertEquals(1, sistema.pesquisaMensagensAnonimas().size());
    }

    @Test
    void testPesquisaTodasAsMensagens() {
        assertTrue(sistema.pesquisaTodasAsMensagens().isEmpty());
        sistema.enviarMensagemParaAlguem("texto 1", "ayla@dcx.ufpb.br", "rodrigor@dcx.ufpb.br", false);
        assertEquals(1, sistema.pesquisaTodasAsMensagens().size());
        sistema.enviarMensagemParaAlguem("texto 2", "ayla@dcx.ufpb.br", "rodrigor@dcx.ufpb.br", true);
        assertEquals(2, sistema.pesquisaTodasAsMensagens().size());
    }

    @Test
    void testPesquisaAmigoEConfiguraAmigoSecretoDe() {
        assertThrows(AmigoInexistenteException.class,
                () -> sistema.pesquisaAmigoSecretoDe("ayla@dcx.ufpb.br"));
        try {
            sistema.cadastraAmigo("Ayla", "ayla@dcx.ufpb.br");
            sistema.cadastraAmigo("Ana", "ana@dcx.ufpb.br");
            sistema.configuraAmigoSecretoDe("ayla@dcx.ufpb.br", "ana@dcx.ufpb.br");
            sistema.configuraAmigoSecretoDe("ana@dcx.ufpb.br", "ayla@dcx.ufpb.br");
            assertEquals("ana@dcx.ufpb.br", sistema.pesquisaAmigoSecretoDe("ayla@dcx.ufpb.br"));
            assertEquals("ayla@dcx.ufpb.br", sistema.pesquisaAmigoSecretoDe("ana@dcx.ufpb.br"));
        } catch (AmigoInexistenteException | AmigoJaExisteException | AmigoNaoSorteadoException e) {
            fail("Não deveria lançar exceção");
        }
    }
}
