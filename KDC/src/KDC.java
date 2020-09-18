import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KDC {
	
	private Pessoa Bob;
	private Pessoa Alice;
	private byte[] Ks_Bob;
	private byte[] Ks_Alice;
	
	public KDC(Pessoa bob, Pessoa alice) {
		super();
		this.Bob = bob;
		this.Alice = alice;
	}

	public Pessoa getBob() {
		return Bob;
	}
	
	public void setBob(Pessoa bob) {
		this.Bob = bob;
	}

	public Pessoa getAlice() {
		return Alice;
	}

	public void setAlice(Pessoa alice) {
		this.Alice = alice;
	}
	
	public byte[] getKs_Bob() {
		return Ks_Bob;
	}

	public void setKs_Bob(byte[] ks_Bob) {
		Ks_Bob = ks_Bob;
	}

	public byte[] getKs_Alice() {
		return Ks_Alice;
	}

	public void setKs_Alice(byte[] ks_Alice) {
		Ks_Alice = ks_Alice;
	}

	public String criaKS() {
		System.out.println("Chave de sessão gerada");		
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}
	
	public void gerarChaveSessao(String origem, byte[] origemCript, byte[] destCript) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		//decifra origemCript na k_bob + compara resultado com origem
		//se deu igual -> gera uma chave de sessão + preenche campos com as chaves de sessão de Bob e Alice
		if(AES.decifra(origemCript, Bob.getChave()).equals(origem)) {
			System.out.println("Autenticação feita com sucesso para o usuário " + Bob.getNome());
			if(AES.decifra(destCript, Bob.getChave()).equals(Alice.getNome())){
				System.out.println("Verificado que " + Bob.getNome() + " deseja falar com " + Alice.getNome());
				String chaveSessao = criaKS();
				this.Ks_Bob = AES.cifra(chaveSessao, Bob.getChave());
				this.Ks_Alice = AES.cifra(chaveSessao, Alice.getChave());
			}
		} else {
			System.out.println("Não foi possível autenticar usuário");
		}
	}
}
