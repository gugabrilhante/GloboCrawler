
public class Artist extends ObjectJson{
	protected String nome;
	protected String nomeShow;
	protected String pais;
	protected String estado;
	protected String cidade;
	protected String dataShow;
	protected String local;
	protected String ingressos;
	protected String classificacao;
	protected String dataExtracao;
	protected String horaExtracao;
	
	
	public Artist() {
		super();
	}
	
	
	
	public String getIngressos() {
		return ingressos;
	}



	public void setIngressos(String ingressos) {
		this.ingressos = ingressos;
	}



	public String getDataExtracao() {
		return dataExtracao;
	}



	public void setDataExtracao(String dataExtracao) {
		this.dataExtracao = dataExtracao;
	}



	public String getHoraExtracao() {
		return horaExtracao;
	}



	public void setHoraExtracao(String horaExtracao) {
		this.horaExtracao = horaExtracao;
	}



	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}



	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeShow() {
		return nomeShow;
	}
	public void setNomeShow(String nomeShow) {
		this.nomeShow = nomeShow;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getDataShow() {
		return dataShow;
	}
	public void setDataShow(String dataShow) {
		this.dataShow = dataShow;
	}
	public String getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	
	

}
