
public class Principal {

	public static void main(String[] args) {

		try {	
			//Define as chaves de Bob e Alice
			String K_Bob 	= "bolabolabolabola";
			String K_Alice	= "patopatopatopato";
			
			//Verifica se as chaves possuem 16 ou 32 caracteres
			if(!(K_Bob.length() == 16 && K_Alice.length() == 16) || (K_Bob.length() == 32 && K_Alice.length() == 32)){ 
				System.out.println("Chaves devem possuir pelo menos 16 caracteres \nEncerrando o sistema");
				System.exit(0);
			}
			
			//Cria Bob e Alice
			Pessoa Bob = new Pessoa("Bob", K_Bob);
			Pessoa Alice = new Pessoa("Alice", K_Alice);
			
			//Registra Bob e Alice no KDC
			System.out.println(Bob.getNome() + " e " + Alice.getNome() + " foram registrados no KDC");
			KDC kdc = new KDC(Bob, Alice);
			
			//Bob manda msg para KDC, pedindo para conversar com Alice	
			System.out.println(Bob.getNome() + " envia mensagem para o KDC, pedindo para falar com " + Alice.getNome());
			kdc.gerarChaveSessao(Bob.getNome(), //P1: identificação de Bob
								AES.cifra(Bob.getNome(), Bob.getChave()), //P2: identificação de Bob cifrado em k_bob
								AES.cifra(Alice.getNome(), Bob.getChave())); //P3: identificação de Alice cifrado em k_bob
			
			//Bob salva a chave de sessão
			Bob.setChaveSessao( AES.decifra(kdc.getKs_Bob(), Bob.getChave()) );
			
			//Alice salva a chave de sessão
			Alice.setChaveSessao( AES.decifra(kdc.getKs_Alice(), Alice.getChave()) );
			System.out.println(Bob.getNome() + " e " + Alice.getNome() + " estão com a chave de sessão: " + Bob.getChaveSessao());
			
			//Alice cria um nonce
			Alice.criaNonce();
			
			//Alice cifra nounce na chave de sessão e manda para Bob
			byte[] nonceCript = AES.cifra("" + Alice.getNonce(), Alice.getChaveSessao());
			System.out.println(Alice.getNome() + " gera um nounce, cifra na chave de sessão e envia para " + Bob.getNome());
			
			//Bob decifra nounce com a chave de sessão
			Bob.setNonce( Integer.parseInt( AES.decifra( nonceCript, Bob.getChaveSessao() ) ) );
			System.out.println(Bob.getNome() + " decifra o nounce com a chave de sessão");
			
			//Bob aplica o nonce na função de autenticação e manda para Alice, cifrado na chave de sessão
			Bob.setNewNonce( Bob.funcAutentic( Bob.getNonce() ) );
			byte[] newNonceCript = AES.cifra("" + Bob.getNewNonce(), Bob.getChaveSessao());
			System.out.println(Bob.getNome() + "aplica o nonce na função de autenticação e manda para " + Alice.getNome() +", cifrado na chave de sessão");
			
			//Alice decifra novo nouce recebido + aplica nonce original na sua função de autenticação -> e compara os dois
			Alice.setNewNonce( Integer.parseInt( AES.decifra( newNonceCript, Alice.getChaveSessao() ) ) );
			if(Alice.getNewNonce() == Alice.funcAutentic(Alice.getNonce())) {
				System.out.println("Mensagem recebida é do " + Bob.getNome());
				System.out.println("Agora " + Bob.getNome() + " e " + Alice.getNome() + " podem trocar mensagens seguras entre si");
			} else {
				System.out.println("Mensagem recebida não é do " + Bob.getNome() + "\nEncerrando o sistema");
				System.exit(0);
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
