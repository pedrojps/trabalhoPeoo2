/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author pedrojorge
 */
public class ArquivoFilme extends Arquivo{

    public ArquivoFilme() {
        super(new File("filmes.xml"));
        if (!arquivo.exists()) {
            try {
		Document doc = new Document();
		Element root = new Element("list");
		doc.setRootElement(root);
		XMLOutputter xout = new XMLOutputter();
			
		xout.output(doc , new FileOutputStream(arquivo));
                System.out.println("Documento criado com sucesso!");
				
            } catch (Exception e) {
		// TODO: handle exception
            }
	}
    }
    
    public String add(String nome,String faixa,String tipo){       
        if(0!=busca(nome,true,"filme").size())
            return "erro nome ja cadastrado";
        SAXBuilder builder = new SAXBuilder();
        try{
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
                
            Element filmeE = new Element("filme");
                
            Attribute nomeE = new Attribute("nome", nome);
            filmeE.setAttribute(nomeE);
            
            Attribute tipoE = new Attribute("tipo", tipo);
            filmeE.setAttribute(tipoE);
                
            Attribute faixaE = new Attribute("faixa", faixa);
            filmeE.setAttribute(faixaE);
            
            root.addContent(filmeE);
                
            XMLOutputter xout = new XMLOutputter();
            OutputStream out = new FileOutputStream(arquivo); 
            xout.output(doc , out);
            System.out.println("Documento alterado com sucesso!");
  
            return "cadastrado com suceso";
        }catch(Exception e){
                e.printStackTrace();
        }
        return "erro cadastral";
    }
    
    public String edita(String nome,String novoNome,String faixa,String tipo){
        String retorno = "erro";
        if(!nome.equals("")){
            if(0==busca(nome,true,"funcinario").size())
                return "erro nao nome ja cadastrado";
            SAXBuilder builder = new SAXBuilder();
            try{
                Document doc= builder.build(arquivo);
                Element root = (Element) doc.getRootElement();
                
                List<Element> filme = super.buscaInterna(root, true, nome,"filme");
                if(!novoNome.equals(""))
                    filme.get(0).setAttribute("nome",novoNome);
                if(!faixa.equals(""))
                    filme.get(0).setAttribute("faixa",faixa);
                if(!tipo.equals(""))
                    filme.get(0).setAttribute("tipo",tipo);
                
                XMLOutputter xout = new XMLOutputter();
		OutputStream out = new FileOutputStream(arquivo); 
		xout.output(doc , out);
		System.out.println("Documento alterado com sucesso!");
  
                retorno = "cadastrado com suceso";
            }catch(Exception e){
            
            }
        }else
            retorno =  "preemcha os compos corretamente";
        return retorno;
    }    
    public String addSesao(String nome,int numeroSala,int horaI,int mimI,int horaF,int mimF){
        String retorno = "erro";
        if(!nome.equals("")){
            if(this.confirnaSesao(numeroSala, horaI, mimI, horaF, mimF)){
                SAXBuilder builder = new SAXBuilder();
                try{
                    Document doc= builder.build(arquivo);
                    Element root = (Element) doc.getRootElement();
                
                    List<Element> filme = super.buscaInterna(root, true, nome,"filme");
                    if(filme.size()==0){
                        return "erro filme n registrado";
                    }
                    List<Element> salas = filme.get(0).getChildren("sala");
                    
                    Element secao = new Element("secao");
                    secao.setAttribute(new Attribute("horaI",""+horaI));
                    secao.setAttribute(new Attribute("mimI",""+mimI));
                    secao.setAttribute(new Attribute("horaF",""+horaF));
                    secao.setAttribute(new Attribute("mimF",""+mimF));
                    
                    boolean confirna = false;
                    for(int i =0; i<salas.size();i++){
                        if(salas.get(i).getAttributeValue("numeroSala").equals(""+numeroSala)){
                            confirna = true;
                            salas.get(i).addContent(secao);
                            break;
                        }
                    }
                    if(!confirna){
                        Element sala = new Element("sala");
                        sala.addContent(secao);
                        filme.get(0).addContent(sala);
                    }
                
                    XMLOutputter xout = new XMLOutputter();
                    OutputStream out = new FileOutputStream(arquivo); 
                    xout.output(doc , out);
                    System.out.println("Documento alterado com sucesso!");
  
                    retorno = "cadastrado com suceso";
                }catch(Exception e){
            
                }
            }else
                retorno = "nesse horario a sala estara ocupada";
        }
        return retorno;
    }
    
    public boolean confirnaSesao(int numeroSala,int horaI,int mimI,int horaF,int mimF){
        SAXBuilder builder = new SAXBuilder();
            try{
                Document doc= builder.build(arquivo);
                Element root = (Element) doc.getRootElement();
                
                List<Element> filme = root.getChildren("filme");
                for(int i=0;i<filme.size();i++ ){
                    List<Element> salas = filme.get(i).getChildren("sala");
                    for(int j = 0 ;j<salas.size();j++){
                        if(salas.get(j).getAttribute("numeroSala").equals(""+numeroSala)){
                            List<Element> secoes = salas.get(j).getChildren("secao");
                            for(int d=0; d<secoes.size();d++){
                                int horaEF = Integer.parseInt(secoes.get(d).getAttributeValue("horaF"));
                                int mimEF = Integer.parseInt(secoes.get(d).getAttributeValue("mimF"));
                                if(horaI >= horaEF && mimI > mimEF+15){
                                    return true;
                                }
                                int horaIF = Integer.parseInt(secoes.get(d).getAttributeValue("horaI"));
                                int mimIF = Integer.parseInt(secoes.get(d).getAttributeValue("mimI"));
                                if(horaF <= horaIF && mimF < mimIF+15){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }catch(Exception e){
                
            }
        return false;
    }
    public List buscaSesao(Element filme,int numeroSala,int horaI,int mimI){
        List<Element> buscado = new LinkedList<>();
        List<Element> salas = filme.getChildren("sala");
        for(int i = 0;i<salas.size();i++){
            if(salas.get(i).getAttribute("numeroSala").equals(""+numeroSala)){
                List<Element> secoes = salas.get(i).getChildren("secao");
                for(int j=0;j<secoes.size();j++){
                    if(secoes.get(j).getAttributeValue("horaI").equals(""+horaI) 
                            &&secoes.get(j).getAttributeValue("mimI").equals(""+mimI)){
                        buscado.add(secoes.get(j));
                    }
                }
            }
        }
        return buscado;
    }
    public List buscaSalas(String nome){
        List<Element> salas = new LinkedList<>(); 
        SAXBuilder builder = new SAXBuilder();
        try{
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
                
            List<Element> filme = super.buscaInterna(root, true, nome, "filme");
            if(filme.size()!=0)
                salas = filme.get(0).getChildren("sala");
        }catch(Exception e){
                    
        }
        return salas;
    }
    public String editaSesao(String nome,int numeroSala,int horaI,int mimI,int horaIN,int mimIN,int horaFN,int mimFN){
        String retorno = "erro";
        SAXBuilder builder = new SAXBuilder();
        if(this.confirnaSesao(numeroSala, horaIN, mimIN, horaFN, mimFN))
            try{
                Document doc= builder.build(arquivo);
                Element root = (Element) doc.getRootElement();
                
                List<Element> filme = super.buscaInterna(root, true, nome, "filme");
                if(filme.size()==0)
                    return "filme n encontrado";
                List<Element> secao = this.buscaSesao(filme.get(0), numeroSala, horaI, mimI);
                if(secao.size()==0)
                    return "sacao n encontrada";
                secao.get(0).setAttribute("horaI",""+horaIN);
                secao.get(0).setAttribute("mimI",""+mimIN);
                secao.get(0).setAttribute("horaF",""+horaFN);
                secao.get(0).setAttribute("mimF",""+mimFN);
                
            }catch (Exception e){
                 
            }
        else
            retorno = "horario indiponivel";
        return retorno;
    }
    public boolean removeSecao(String nome,int numeroSala,int horaI,int mimI){
        try{
            SAXBuilder builder = new SAXBuilder();
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
                
            List<Element> filme = super.buscaInterna(root, true, nome, "filme");
            if(filme.size() == 0)
                return false;
            List<Element> removido = this.buscaSesao(filme.get(0), numeroSala, horaI, mimI);
            if(removido.size() == 0)
                return false;
            filme.get(0).removeContent(removido.get(0));
            return true;
        }catch(Exception e){
            
        }
        return false;
    }
}
