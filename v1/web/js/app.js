document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault();

    const form = event.target;

    const data = {
        nome: form.nome.value,
        email: form.email.value,
        sexo: form.sexo.value,
        telefone: form.telefone.value,
        dataNascimento: form.dataNascimento.value,
        endereco: {
            rua: form["endereco.rua"].value,
            numero: parseInt(form["endereco.numero"].value),
            complemento: form["endereco.complemento"].value,
            cidade: form["endereco.cidade"].value,
            estado: form["endereco.estado"].value,
            cep: form["endereco.cep"].value,
        },
        experiencias: [
            {
                cargo: form["experiencias[0].cargo"].value,
                empresa: form["experiencias[0].empresa"].value,
                dataInicio: form["experiencias[0].dataInicio"].value,
                dataFim: form["experiencias[0].dataFim"].value,
            }
        ],
        educacoes: [
            {
                grau: form["educacoes[0].grau"].value,
                curso: form["educacoes[0].curso"].value,
                instituicao: form["educacoes[0].instituicao"].value,
                dataInicio: form["educacoes[0].dataInicio"].value,
                dataFim: form["educacoes[0].dataFim"].value,
            }
        ],
        habilidades: [
            {
                descricao: form["habilidades[0].descricao"].value,
                nivel: form["habilidades[0].nivel"].value,
                especialidade: form["habilidades[0].especialidade"].value,
            }
        ]
    };

    fetch('http://localhost:8080/candidato', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao enviar");
            return resp.json();
        })
        .then(result => {
            alert("Dados enviados com sucesso!");
            console.log(result);
        })
        .catch(err => {
            alert("Falha no envio: " + err.message);
            console.error(err);
        });
});