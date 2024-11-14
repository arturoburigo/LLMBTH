def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.LICENCA_COM_VENCIMENTOS
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
    if ( existeAfastamento ) continue;
    existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
