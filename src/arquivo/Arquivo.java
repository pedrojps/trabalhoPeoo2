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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author pedrojorge
 */
public class Arquivo {
    protected File arquivo;
    
    public Arquivo(File arquivo){
        this.arquivo = arquivo;
    }
           
    public List busca(String nome,boolean completa,String nomeE){
        SAXBuilder builder = new SAXBuilder();
        List<Element> retorna = new LinkedList<>();
        try {
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
            
            retorna = this.buscaInterna(root, completa, nome,nomeE);
        }catch(Exception e){
            
        }
        return retorna;
    } 
    protected List buscaInterna(Element root,boolean completa,String nome,String nomeE){
        List<Element> retorna = new LinkedList<>();            
        List<Element> empregados = root.getChildren(nomeE);
        for (Element empregado : empregados) {
            if(completa){
                if(empregado.getAttributeValue("nome").equals(nome))
                    retorna.add(empregado);
                }else{
                    if(empregado.getAttributeValue("nome").contains(nome))
                        retorna.add(empregado);
                }        
            }
        return retorna;
    }
    
    public String remove(String nome,String nomeE){
        SAXBuilder builder = new SAXBuilder();
        List<Element> retorna = new LinkedList<>();
        
        try {
            Document doc= builder.build(arquivo);
            Element root = (Element) doc.getRootElement();
            
            List<Element> removido = buscaInterna(root,true,nome,nomeE);
            root.removeContent(removido.get(0));
            
            XMLOutputter xout = new XMLOutputter();
            OutputStream out = new FileOutputStream(arquivo); 
            xout.output(doc , out);
            return "Documento alterado com sucesso!";
        }catch(Exception e){
            e.printStackTrace();
        }       
        
        return "n foi possivel remover "+nome;
    }
}
