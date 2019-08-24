package com.example.ezgrocery.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.CupomDesconto;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Receita;

import java.util.ArrayList;

public class OperacaoBancoDeDados {

    private SQLiteDatabase bancoDados;

    public OperacaoBancoDeDados(Context ctx)
    {
        bancoDados = ctx.openOrCreateDatabase("ezGrocery", ctx.MODE_PRIVATE, null);

        //deletarTudo();

        criarTabelas();
        preencherTabelaReceita();
        preencherTabelaProdutos();
        preencherTabelaCupom();
    }

    public void criarTabelas(){


        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS receitas(id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, descricao VARCHAR, idListaProduto INTEGER ) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS produtos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cod_ref VARCHAR," +
                "preco int) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS listaProdutos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, usuarioId VARCHAR, comprada INTEGER DEFAULT 0 ) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS produtosdaLista(id INTEGER PRIMARY KEY AUTOINCREMENT, idLista INTEGER, idProduto INTEGER ) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS cupomDesconto(id INTEGER PRIMARY KEY AUTOINCREMENT, descricao VARCHAR, codigo VARCHAR, valor INTEGER ) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS carteiraVirtual(id INTEGER PRIMARY KEY AUTOINCREMENT, valor INTERGER, usuarioId VARCHAR ) ");
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS cuponsCarteira(id INTEGER PRIMARY KEY AUTOINCREMENT, carteiraId INTEGER, cupomDescontoId INTEGER) ");
    }

    private void deletarTudo(){

        bancoDados.execSQL("Drop table receitas");
        bancoDados.execSQL("Drop table produtos");
        bancoDados.execSQL("Drop table listaProdutos");
        bancoDados.execSQL("Drop table produtosdaLista");
        bancoDados.execSQL("Drop table cupomDesconto");
        bancoDados.execSQL("Drop table carteiraVirtual");
        bancoDados.execSQL("Drop table cuponsCarteira");

       /* bancoDados.delete("receitas",null,null);
        bancoDados.delete("produtos",null,null);
        bancoDados.delete("listaProdutos",null,null);
        bancoDados.delete("produtosdaLista",null,null);*/
    }

    public Receita obterReceita(int receitaId){

        Receita receita = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM receitas where Id = " + receitaId,null);

            //recuperar os ids das colunas
            int indiceColunaDescricao = cursor.getColumnIndex("descricao");
            int indiceColunaNomeReceita= cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaListaProdutos = cursor.getColumnIndex("idListaProduto");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeReceita);
                String descricao = cursor.getString(indiceColunaDescricao);
                int id = cursor.getInt(indiceColunaId);
                int listaProdutosid = cursor.getInt(indiceColunaListaProdutos);

                receita = new Receita(id,nome,descricao);

                receita.listadeProdutos = obterListadeProdutos(listaProdutosid);

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return receita;
    }

    public CarteiraVirtual obterCarteira(String usuarioId){

        CarteiraVirtual carta = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM carteiraVirtual where usuarioId = '" + usuarioId +"'",null);

            //recuperar os ids das colunas
            int indiceColunaValor= cursor.getColumnIndex("valor");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaUsuarioId = cursor.getColumnIndex("usuarioId");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String usuario = cursor.getString(indiceColunaUsuarioId);
                int id = cursor.getInt(indiceColunaId);
                int valor = cursor.getInt(indiceColunaValor);

                carta = new CarteiraVirtual();
                carta.id = id;
                carta.usuarioId = usuario;
                carta.valor = valor;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return carta;
    }

    public Produto obterProduto(int produtoId){

        Produto prod = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM produtos where Id = " + produtoId,null);

            //recuperar os ids das colunas
            int indiceColunaCodigo = cursor.getColumnIndex("cod_ref");
            int indiceColunaNome= cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaPreco = cursor.getColumnIndex("preco");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNome);
                String codigo = cursor.getString(indiceColunaCodigo);
                int id = cursor.getInt(indiceColunaId);
                int preco = cursor.getInt(indiceColunaPreco);

                prod = new Produto();

                prod.preco = preco;
                prod.cod_ref = codigo;
                prod.nome = nome;
                prod.id = id;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return prod;
    }
    public CupomDesconto obterCupom(String codigo){

        CupomDesconto cup = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM cupomDesconto where codigo = '" + codigo+"'",null);

            //recuperar os ids das colunas
            int indiceColunaCodigo = cursor.getColumnIndex("codigo");
            int indiceColunaDescricao= cursor.getColumnIndex("descricao");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaValor = cursor.getColumnIndex("valor");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String descricao = cursor.getString(indiceColunaDescricao);
                String codigoB = cursor.getString(indiceColunaCodigo);
                int id = cursor.getInt(indiceColunaId);
                int valor = cursor.getInt(indiceColunaValor);

                cup = new CupomDesconto();

                cup.codigo = codigoB;
                cup.descricao = descricao;
                cup.valor = valor;
                cup.id = id;
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return cup;
    }
    public CupomDesconto obterCupomPorId(int cupomId){

        CupomDesconto cup = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM cupomDesconto where id = '" + cupomId+"'",null);

            //recuperar os ids das colunas
            int indiceColunaCodigo = cursor.getColumnIndex("codigo");
            int indiceColunaDescricao= cursor.getColumnIndex("descricao");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaValor = cursor.getColumnIndex("valor");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String descricao = cursor.getString(indiceColunaDescricao);
                String codigoB = cursor.getString(indiceColunaCodigo);
                int id = cursor.getInt(indiceColunaId);
                int valor = cursor.getInt(indiceColunaValor);

                cup = new CupomDesconto();

                cup.codigo = codigoB;
                cup.descricao = descricao;
                cup.valor = valor;
                cup.id = id;
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return cup;
    }
    public ArrayList<Receita> recuperarReceitas(){

        ArrayList<Receita> receitas = new ArrayList<>();
        try{
            //Recuperar as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM receitas ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaDescricao = cursor.getColumnIndex("descricao");
            int indiceColunaNomeReceita= cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");

            //listar as tarefas
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeReceita);
                String descricao = cursor.getString(indiceColunaDescricao);
                int id = cursor.getInt(indiceColunaId);

                Receita receita = new Receita(id,nome,descricao);
                receitas.add(receita);

                if(cursor.isLast())
                    break;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return receitas;
    }
    public ArrayList<CupomDesconto> recuperarCupoms(){

        ArrayList<CupomDesconto> cupons = new ArrayList<>();
        try{
            //Recuperar as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM cupomDesconto ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaCodigo = cursor.getColumnIndex("codigo");
            int indiceColunaDescricao= cursor.getColumnIndex("descricao");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaValor = cursor.getColumnIndex("valor");

            //listar as tarefas
            cursor.moveToFirst();
            while ( cursor != null ){
                String descricao = cursor.getString(indiceColunaDescricao);
                String codigoB = cursor.getString(indiceColunaCodigo);
                int id = cursor.getInt(indiceColunaId);
                int valor = cursor.getInt(indiceColunaValor);

                CupomDesconto cup = new CupomDesconto();

                cup.codigo = codigoB;
                cup.descricao = descricao;
                cup.valor = valor;
                cup.id = id;
                cupons.add(cup);

                if(cursor.isLast())
                    break;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return cupons;
    }
    public ListadeProdutos obterListadeProdutos(int listaId){

        ListadeProdutos lista = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM listaProdutos " +
                    "where Id = " + listaId,null);

            //recuperar os ids das colunas
            int indiceColunaNomeLista = cursor.getColumnIndex("nome");
            int indiceColunaComprada = cursor.getColumnIndex("comprada");
            int indiceColunaId = cursor.getColumnIndex("id");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeLista);
                int id = cursor.getInt(indiceColunaId);

                ArrayList<Produto> produtos = recuperarProdutosdaLista(id);

                lista = new ListadeProdutos(id,nome, produtos);
                lista.comprada = cursor.getInt(indiceColunaComprada) == 1 ? true : false;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return lista;
    }
    public ArrayList<ListadeProdutos> recuperarListasProdutos(String userId){

        ArrayList<ListadeProdutos> listas = new ArrayList<>();
        try{
            //Recuperar as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM listaProdutos WHERE usuarioId = '"+userId+"' ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaNomeLista = cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaComprada = cursor.getColumnIndex("comprada");

            //listar
            cursor.moveToFirst();
            int icount = cursor.getInt(0);

            while ( cursor != null && icount != 0){
                String nome = cursor.getString(indiceColunaNomeLista);
                int id = cursor.getInt(indiceColunaId);

                ListadeProdutos lista = new ListadeProdutos(id,nome,new ArrayList<Produto>());
                lista.comprada = cursor.getInt(indiceColunaComprada) == 1 ? true : false;
                listas.add(lista);

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listas;
    }
    public ArrayList<Produto> recuperarTodosProdutos(){

        ArrayList<Produto> listas = new ArrayList<>();
        try{
            //Recuperar as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM produtos ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaNomeLista = cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaPreco = cursor.getColumnIndex("preco");
            int indiceColunaCodRef = cursor.getColumnIndex("cod_ref");

            //listar
            cursor.moveToFirst();
            int icount = cursor.getInt(0);

            while ( cursor != null && icount != 0){
                String nome = cursor.getString(indiceColunaNomeLista);
                String codRef = cursor.getString(indiceColunaCodRef);
                int id = cursor.getInt(indiceColunaId);
                int preco = cursor.getInt(indiceColunaPreco);

                Produto produto = new Produto();
                produto.id = id;
                produto.nome = nome;
                produto.cod_ref = codRef;
                produto.preco = preco;

                listas.add(produto);

                if(cursor.isLast())
                    break;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listas;
    }
    public ArrayList<Produto> recuperarProdutosdaLista(int listaId){

        ArrayList<Produto> prods = new ArrayList<>();
        try{
            //Recuperar as tarefas
            Cursor cursor = bancoDados.rawQuery("SELECT p.* FROM produtosdaLista pl " +
                    "inner join produtos p on p.Id = pl.idProduto Where pl.idLista = " + listaId, null);

            //recuperar os ids das colunas
            int indiceColunaNomeLista = cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaPreco = cursor.getColumnIndex("preco");
            int indiceColunaCodRef = cursor.getColumnIndex("cod_ref");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeLista);
                String codRef = cursor.getString(indiceColunaCodRef);
                int id = cursor.getInt(indiceColunaId);
                int preco = cursor.getInt(indiceColunaPreco);

                Produto produto = new Produto();

                produto.id = id;
                produto.nome = nome;
                produto.cod_ref = codRef;
                produto.preco = preco;

                prods.add(produto);

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return prods;
    }
    public void cadastrarCupom(String descricao, String codigo, int valor){
        ContentValues values = new ContentValues();
        values.put("descricao", descricao);
        values.put("codigo", codigo);
        values.put("valor", valor);

        bancoDados.insert("cupomDesconto", null, values);
    }
    public void cadastrarCupomNaCarteira(int carteiraId, int cupomId){
        ContentValues values = new ContentValues();
        values.put("carteiraId", carteiraId);
        values.put("cupomDescontoId", cupomId);

        bancoDados.insert("cuponsCarteira", null, values);
    }
    public void atualizarCupom(int cupomId, String descricao, String codigo, int valor){
        ContentValues values = new ContentValues();
        values.put("descricao", descricao);
        values.put("codigo", codigo);
        values.put("valor", valor);

        bancoDados.update("cupomDesconto", values, "id = " + cupomId,null);
    }

    public void comprarLista(int listaProdutosId){
        ContentValues values = new ContentValues();
        values.put("comprada", 1);

        bancoDados.update("listaProdutos", values, "id = " + listaProdutosId,null);
    }
    public void cadastrarCarteiraVazia(String usuarioId)
    {
        ContentValues values = new ContentValues();
        values.put("usuarioId", usuarioId);
        values.put("valor", 0);

        bancoDados.insert("carteiraVirtual", null, values);
    }

    public void atualizarCarteira(int carteiraId, int novoValor)
    {
        ContentValues values = new ContentValues();
        values.put("valor", novoValor);

        bancoDados.update("carteiraVirtual", values, "id = " + carteiraId,null);
    }
    public void cadastrarReceita(String nome, String descricao)
    {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("descricao", descricao);

        bancoDados.insert("receitas", null, values);
    }

    public void atualizarReceita(int receitaId, String nome, String descricao)
    {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("descricao", descricao);

        bancoDados.update("receitas", values, "id = " + receitaId,null);
    }

    public void atualizarProduto(int produtoId, String nome, String codigo, int preco)
    {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("cod_ref", codigo);
        values.put("preco", preco);

        bancoDados.update("produtos", values, "id = " + produtoId,null);
    }
    public void atualizarListaDeProdutosdaReceita(int receitaId, int idListaProdutos)
    {
        ContentValues values = new ContentValues();
        values.put("idListaProduto", idListaProdutos);

        bancoDados.update("receitas", values, "id = " + receitaId,null);
    }

    public void deletarReceita(int idReceita)
    {
        bancoDados.execSQL("DELETE FROM receitas WHERE id ="+idReceita);
    }

    public void deletarCupomDaCarteira(int cupomDescontoId)
    {
        bancoDados.execSQL("DELETE FROM cuponsCarteira WHERE id ="+cupomDescontoId);
    }

    public void deletarProduto(int idProduto)
    {
        bancoDados.execSQL("DELETE FROM produtos WHERE id ="+idProduto);
    }

    public void deletarCupom(int cupomId)
    {
        bancoDados.execSQL("DELETE FROM cupomDesconto WHERE id ="+cupomId);
        deletarCupomDaCarteira(cupomId);
    }

    public void cadastrarProduto(String nome, String codigo, int preco){

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("cod_ref", codigo);
        values.put("preco", preco);

        bancoDados.insert("produtos", null, values);
    }

    public void cadastrarListaProdutos(String nomeLista, String userId){

        ContentValues values = new ContentValues();
        values.put("nome", nomeLista);
        values.put("usuarioId", userId);

        bancoDados.insert("listaProdutos", null, values);
    }

    public void cadastrarProdutosALista(int idLista, int idProduto ){

        ContentValues values = new ContentValues();
        values.put("idLista", idLista);
        values.put("idProduto", idProduto);

        bancoDados.insert("produtosdaLista", null, values);
    }

    public void deletarlistaProdutos(int listaid){

        bancoDados.execSQL("DELETE FROM listaProdutos WHERE id ="+listaid);
    }

    public void deletarProdutoDeUmaLista(int listaId, int produtoId){

        bancoDados.execSQL("DELETE FROM produtosdaLista WHERE id = " +
                "(SELECT min(id) FROM produtosdaLista WHERE idLista ="+listaId+" AND idProduto = " + produtoId+")");
    }

    public boolean cupomJaAdicionadoNaCarteira(int cupomId, int carteiraId){

        boolean retorno = false;

        try{
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM cuponsCarteira where cupomDescontoId = " + cupomId +
                    " and carteiraId = " + carteiraId,null);

            int indiceColunaId = cursor.getColumnIndex("id");
            cursor.moveToFirst();
            while ( cursor != null ){
                int id = cursor.getInt(indiceColunaId);
                retorno =true;
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return retorno;
    }

    public void preencherTabelaReceita(){

        String count = "SELECT count(*) FROM receitas";
        Cursor mcursor = bancoDados.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            //Pre cadastrar outras receitas aqui

            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Leite','codQwewe',2)");
            Produto leite  = obterUltimoProduto();

            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Baunilha','codPovewe',1)");
            Produto baunilha  = obterUltimoProduto();

            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Ovos','cod21adssd',10)");
            Produto ovos = obterUltimoProduto();

            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Leite Condensado','cod78asdwe',3)");
            Produto leiteCondensado = obterUltimoProduto();

            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('AÃ§ucar','cod54645a',3)");
            Produto acucar = obterUltimoProduto();


            cadastrarListaProdutos("Lista de produtos da receita Ovos Nevados", null);
            ListadeProdutos lpOvos = obterUltimaListadeProdutos();

            cadastrarProdutosALista(lpOvos.id, leite.id);
            cadastrarProdutosALista(lpOvos.id, baunilha.id);
            cadastrarProdutosALista(lpOvos.id, ovos.id);

            bancoDados.execSQL("INSERT INTO receitas (idListaProduto, nome,descricao ) VALUES("+lpOvos.id+", 'Ovos Nevados','" +
                    "INGREDIENTES\n" +
                    "1 litro de leite integral\n" +
                    "6 claras\n" +
                    "6 gemas\n" +
                    "12 colheres (sopa) de aÃ§Ãºcar (cheias)\n" +
                    "3 gotas de essÃªncia de baunilha\n\n\n" +

                    "MODO DE PREPARO\n" +
                    "Bata as claras em neve atÃ© que fiquem bem firmes. Acrescente, uma a uma, as 6 colheres de aÃ§Ãºcar, continue batendo atÃ© obter um suspiro. Coloque o leite para ferver, abaixe o fogo e cozinhe as claras em neve, colocadas em colheradas.\n" +
                    "Deixe 15 segundos de cada lado, vire com cuidado para nÃ£o quebrar. ApÃ³s o cozimento retire com uma escumadeira e coloque em uma compoteira ou um recipiente fundo. Reserve.\n" +
                    "Bata as gemas com as outras 6 colheres de aÃ§Ãºcar e a essÃªncia de baunilha, atÃ© que forme um creme.\n" +
                    "Desligue a batedeira, acrescente o leite do cozimento e leve ao fogo para engrossar, mexendo sempre, sem deixar ferver. Retire do fogo, continue mexendo atÃ© que esfrie (mexa para nÃ£o talhar), jogue por cima das nuvens de suspiros cozidas. Leve para gelar e sirva.\n" +
                    "Outra sugestÃ£o de ovos nevados:\n" +
                    "Bata as claras em neve em ponto de suspiro e reserve. Ferva o leite com o aÃ§Ãºcar a gosto, abaixe o fogo, coloque as claras em neve em colheradas , deixe 15 segundos e retire com auxÃ­lio de uma escumadeira. Coloque em um recipiente fundo. Reserve. Acrescente ao leite do cozimento o leite condensado, as gemas passadas pela peneira, o amido de milho, a baunilha e leve ao fogo para engrossar. Desligue o fogo.\n" +
                    "Jogue o creme sobre os ovos nevados, polvilhe com canela em pÃ³ e leve para gelar.') ");


            cadastrarListaProdutos("Lista de produtos da receita Pudim de Leite", null);
            ListadeProdutos lpPudim = obterUltimaListadeProdutos();

            cadastrarProdutosALista(lpPudim.id, leiteCondensado.id);
            cadastrarProdutosALista(lpPudim.id, acucar.id);
            cadastrarProdutosALista(lpPudim.id, ovos.id);

            bancoDados.execSQL("INSERT INTO receitas (idListaProduto, nome,descricao ) VALUES("+lpPudim.id+", 'Pudim de Leite','" +
                    "INGREDIENTES\n" +
                    "6	colheres (sopa) de aÃ§Ãºcar\n" +
                    "3 ovos\n"+
                    "1	lata de leite condensado + a mesma medida de leite\n\n\n"+

                    "MODO DE PREPARO\n"+
                    "Caramelize uma fÃ´rma de cone central (20 cm de diÃ¢metro) com o aÃ§Ãºcar em fogo mÃ©dio, espalhando por todo o interior. Reserve.\n"+
                    "No liquidificador, bata os ovos atÃ© espumar. Acrescente o leite condensado e o leite e bata bem (1 minuto).\n"+
                    "Deixe em repouso por 15 minutos (para a espumar assentar) e despeje na fÃ´rma (nÃ£o deixe cair espuma).\n"+
                    "Leve ao banho-maria no fogÃ£o ou no forno por cerca de 1 hora ou atÃ© firmar.\n"+
                    "Espere esfriar e leve Ã  geladeira por cerca de 2 horas antes de desenformar.')");
        }
    }

    public void preencherTabelaProdutos(){

        String count = "SELECT count(*) FROM produtos";
        Cursor mcursor = bancoDados.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Pera','cod87awre',3)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('MaÃ§Ã£','cod2wdas1',3)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Banana','codwqdvv1',2)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Queijo','cod87awvrf31',15)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Tomate','cod21adsdwae',3)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('Leite Condensado','cod78asdwe',3)");
            bancoDados.execSQL("INSERT INTO produtos (nome, cod_ref, preco) VALUES('AÃ§ucar','cod54645a',3)");

        }
    }

    public void preencherTabelaCupom(){

        String count = "SELECT count(*) FROM cupomDesconto";
        Cursor mcursor = bancoDados.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            bancoDados.execSQL("INSERT INTO cupomDesconto (descricao, codigo, valor) VALUES('Desconto de R$ 10','desconto',10)");
            bancoDados.execSQL("INSERT INTO cupomDesconto (descricao, codigo, valor)VALUES('Desconto de R$ 30','jkldjfPO',30)");
            bancoDados.execSQL("INSERT INTO cupomDesconto (descricao, codigo, valor)VALUES('Desconto de R$ 50','uonfoewRWE',50)");
        }
    }

    public ListadeProdutos obterUltimaListadeProdutos(){

        ListadeProdutos lista = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM listaProdutos " +
                    "where Id = (Select max(id) from listaProdutos)",null);

            //recuperar os ids das colunas
            int indiceColunaNomeLista = cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeLista);
                int id = cursor.getInt(indiceColunaId);

                ArrayList<Produto> produtos = recuperarProdutosdaLista(id);

                lista = new ListadeProdutos(id,nome, produtos);

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return lista;
    }

    public Receita obterUltimaReceita(){

        Receita receita = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM receitas " +
                    "where Id = (Select max(id) from receitas)",null);

            //recuperar os ids das colunas
            int indiceColunaNomeReceita = cursor.getColumnIndex("nome");
            int indiceColunaDescricaoReceita = cursor.getColumnIndex("descricao");
            int indiceColunaId = cursor.getColumnIndex("id");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeReceita);
                String desc = cursor.getString(indiceColunaDescricaoReceita);
                int id = cursor.getInt(indiceColunaId);

                receita = new Receita(id,nome,desc);

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return receita;
    }


    private Produto obterUltimoProduto(){

        Produto prod = null;

        try{
            //Recuperar
            Cursor cursor = (Cursor) bancoDados.rawQuery("SELECT * FROM Produtos " +
                    "where Id = (Select max(id) from Produtos)",null);

            //recuperar os ids das colunas
            int indiceColunaNomeProduto = cursor.getColumnIndex("nome");
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaCodigo= cursor.getColumnIndex("cod_ref");
            int indiceColunaPreco = cursor.getColumnIndex("preco");

            //listar
            cursor.moveToFirst();
            while ( cursor != null ){
                String nome = cursor.getString(indiceColunaNomeProduto);
                int id = cursor.getInt(indiceColunaId);
                String codigo = cursor.getString(indiceColunaCodigo);
                int preco = cursor.getInt(indiceColunaPreco);

                prod = new Produto();

                prod.preco = preco;
                prod.cod_ref = codigo;
                prod.id = id;
                prod.nome = nome;

                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return prod;
    }


}
