// Função para buscar e preencher o endereço baseado no CEP
function buscarCep() {
    var cep = document.getElementById("cep").value.replace(/\D/g, '');
    if (cep.length == 8) {
        fetch(`https://viacep.com.br/ws/${cep}/json/`)
            .then(response => response.json())
            .then(data => {
                if (!data.erro) {
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

function adicionar(containerId, campos) {
    const container = document.getElementById(containerId);
    const camposArray = campos.split(',');

    // Determina o próximo índice com base no número de elementos já existentes
    const index = container.querySelectorAll('.entry').length;

    const div = document.createElement('div');
    div.className = 'entry';

    camposArray.forEach(campo => {
        const label = document.createElement('label');
        label.textContent = campo.charAt(0).toUpperCase() + campo.slice(1) + ':';

        const input = document.createElement('input');

        // Define o nome no padrão que o Spring/Thymeleaf reconhece
        input.name = `${containerId}[${index}].${campo}`;
        input.placeholder = campo;
        input.required = true;

        if (campo.toLowerCase().includes('data')) {
            input.type = 'date';
        } else {
            input.type = 'text';
        }

        div.appendChild(label);
        div.appendChild(input);
    });

    const btn = document.createElement('button');
    btn.type = 'button';
    btn.textContent = 'Remover';
    btn.onclick = function() {
        remover(btn);
        reindexarCampos(containerId); // atualiza os índices após remoção
    };

    div.appendChild(btn);
    container.appendChild(div);
}

// Atualiza os índices após remoção de um item
function reindexarCampos(containerId) {
    const container = document.getElementById(containerId);
    const entries = container.querySelectorAll('.entry');

    entries.forEach((entry, newIndex) => {
        const inputs = entry.querySelectorAll('input');
        inputs.forEach(input => {
            const parts = input.name.split('.');
            const prop = parts[1]; // ex: cargo, dataInicio, etc.
            input.name = `${containerId}[${newIndex}].${prop}`;
        });
    });
}

function remover(botao) {
    const entry = botao.parentNode;
    entry.remove();
}