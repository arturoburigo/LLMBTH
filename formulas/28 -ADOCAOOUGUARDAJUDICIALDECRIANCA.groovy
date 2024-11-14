def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.ADOCAO_GUARDA_JUDICIAL_DE_CRIANCA
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
    if ( existeAfastamento ) continue;
    existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
