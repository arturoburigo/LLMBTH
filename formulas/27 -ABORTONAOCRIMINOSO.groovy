def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.ABORTO_NAO_CRIMINOSO
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
    if ( existeAfastamento ) continue;
    existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
