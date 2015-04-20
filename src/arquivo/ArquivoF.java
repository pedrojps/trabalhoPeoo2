/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arquivo;

import java.io.*;
import java.util.LinkedList;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import java.util.List;
/**
 *
 * @author pedrojorge
 */
public class ArquivoF extends Arquivo{
    
    public ArquivoF(){
        super(new File("empregados.xml"));
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
    
    public String add(String confSenha,String loing ,String senha, String nome){
        if(confSenha.equals(senha) && !loing.equals("") && !nome.equals("") && !senha.equals("")){
            if(0!=busca(nome,true,"funcinario").size()&&buscaLong(loing).size()!=0)
                return "erro nome ja cadastrado";
            SAXBuilder builder = new SAXBuilder();
            try{
                Document doc= builder.build(arquivo);
                Element root = (Element) doc.getRootElement();
            
                Element funcionario = new Element("funcinario");
                
                Attribute nomeE = new Attribute("nome", nome);
                funcionario.setAttribute(nomeE);
                
                Element loingE = new Element("loing");
                loingE.setText(loing);
                funcionario.addContent(loingE);
                
                Element senhaE = new Element("senha");
                senhaE.setText(senha);
                funcionario.addContent(senhaE);
                
                root.addContent(funcionario);
                
                XMLOutputter xout = new XMLOutputter();
		OutputStream out = new FileOutputStream(arquivo); 
		xout.output(doc , out);
		System.out.println("Documento alterado com sucesso!");
  
                return "cadastrado com suceso";
            }catch(Exception e){
            
            }
        }else
            return  "preemcha os compos corretamente";
        return "erro cadastral";
    }
    public boolean confirma(String senha, String loing) {
        List<Element> comfir = buscaLong(loing);
        if(comfir.size()!=0){
           if(comfir.get(0).getChildText("senha").equals(senha))
               return true;
        }
        return false;
    }
    public String edita(String confSenha,String loing ,String senha, String nome){
        if(confSenha.equals(senha) && !loing.equals("") && !senha.equals("")){
            if(0==busca(nome,true,"funcinario").size() && buscaLong(loing).size()!=0)
                return "erro nome nao cadastrado";
            SAXBuilder builder = new SAXBuilder();
            try{
                Document doc= builder.build(arquivo);
                Element root = (Element) doc.getRootElement();
                
                List<Element> funcinario = super.buscaInterna(root, true, nome,"funcinario");
                if(!loing.equals(""))
                    funcinario.get(0).getChild("loing").setText(loing);
                if(!senha.equals(""))
                    funcinario.get(0).getChild("senha").setText(senha);
                
                XMLOutputter xout = new XMLOutputter();
		OutputStream out = new FileOutputStream(arquivo); 
		xout.output(doc , out);
		System.out.println("Documento alterado com sucesso!");
  
                return "cadastrado com suceso";
            }catch(Exception e){
            
            }
        }else
            return  "preemcha os compos corretamente";
        return "erro cadastral";
    }
    
    public List buscaLong(String loing){
        
        List<Element> loings = new LinkedList<>();
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
                
            List<Element> funcinario = super.buscaInterna(root, false, "","funcinario");
            for(int i=0;i<funcinario.size();i++){
                if(funcinario.get(i).getChildText("loing").equals(loing))
                    loings.add(funcinario.get(i));
            }
        }catch(Exception e){
                
        }
        return loings;
    }
    
}
