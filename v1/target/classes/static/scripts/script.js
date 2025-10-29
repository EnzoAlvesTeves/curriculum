// Função para buscar e preencher o endereço baseado no CEP
function buscarCep() {
    var cep = document.getElementById("cep").value.replace(/\D/g, ''); // Remove caracteres não numéricos
    if (cep.length == 8) {
        // Faz a requisição para a API ViaCEP
        fetch(`https://viacep.com.br/ws/${cep}/json/`)
            .then(response => response.json())
            .then(data => {
                if (!data.erro) {
                    // Preenche os campos do endereço com os dados da API
                    document.getElementById("rua").value = data.logradouro;
                    document.getElementById("bairro").value = data.bairro;
                    document.getElementById("cidade").value = data.localidade;
                    document.getElementById("estado").value = data.uf;
                } else {
                    alert("CEP não encontrado.");
                }
            })
            .catch(error => alert("Erro ao buscar o CEP."));
    } else {
        alert("CEP inválido.");
    }
}
