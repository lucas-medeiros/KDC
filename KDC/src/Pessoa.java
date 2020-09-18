import java.util.Random;


/*
 * @author 	Lucas Cardoso de Medeiros
 * @since 	18/09/2020
 * @version 1.0.0
 */


public class Pessoa {
	
	private String nome;
	private String chave;
	private String chaveSessao;
	int nonce;
	int newNonce;
	
	public Pessoa(String nome, String chave) {
		super();
		this.nome = nome;
		this.chave = chave;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getChave() {
		return chave;
	}
	
	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getChaveSessao() {
		return chaveSessao;
	}

	public void setChaveSessao(String chaveSessao) {
		this.chaveSessao = chaveSessao;
	}
	
	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nounce) {
		this.nonce = nounce;
	}

	public int getNewNonce() {
		return newNonce;
	}

	public void setNewNonce(int newNonce) {
		this.newNonce = newNonce;
	}

	public void criaNonce() {
		Random rand = new Random();
		this.nonce = rand.nextInt(1000); //retorna um número aleatório entre 0-1000
	}
	
	public int funcAutentic(int nonce) {
		return (nonce * 27) + 9;
	}
	
}
