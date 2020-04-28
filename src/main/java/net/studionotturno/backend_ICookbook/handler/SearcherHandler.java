package net.studionotturno.backend_ICookbook.handler;


import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.LazyResource;
import net.studionotturno.backend_ICookbook.iterators.ConcreteCollection;
import net.studionotturno.backend_ICookbook.iterators.MyIterator;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fa parte del desing patter Chain of responsability per le chiamate dal client;<br/>
 * questo concrateHandler realizza le elaborazioni in base alle richieste
 * per le ricerche di ricette secondo vari campi
 *
 * @see net.studionotturno.backend_ICookbook.controllers.SearcherController
 * @see net.studionotturno.backend_ICookbook.iterators.MyIterator
 * @see net.studionotturno.backend_ICookbook.iterators.IterableCollection
 *
 * */
public class SearcherHandler implements Handler {

    private HttpServletRequest request;
    private Document body;
    private String email;

    /**
     * Costruttore; vengono estrapolati alcuni dati dalla request per essere usati nei metodi;
     * per i metodi nel costruttore viene effettuata una ricerca dinamica e vengono usati i metodi
     * del'interfaccia Handler con metodi default(con corpo)
     *
     * @see Handler
     * */
    public SearcherHandler(HttpServletRequest request, Handler next)  {
        this.request=request;
        this.body=getBody(request);
        this.email=getEmail(request.getHeader("header"));
    }

    @Override
    public ResponseEntity<?> elab(){
        String opt=this.request.getRequestURI();
        if(opt.contains("docu/iterator/byName/")) return searchByName();
        else if(opt.contains("docu/iterator/byIngredient/")) return searchByIngredient();
        else if(opt.contains("docu/iterator/byExecutionTime/")) return searchByExecutionTime();
        else if(opt.contains("docu/iterator/byDifficult/")) return searchByDifficult();
        else if(opt.contains("docu/iterator/byTotalTags/")) return searchByTotalTags();
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Istanziazione di un ConcreteIterator per e ricette in base al nome<br/>
     * Vengono cercate tutte le ricette che contengono un certo nome
     * @return un set di ricette in formato Lazy
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByName
     */
    public ResponseEntity<?> searchByName(){
        Set<LazyResource> lazySet= decodeSet(this.body.get("res"));
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection()
                .createConcreteIteratorByName(lazySet, (String) this.body.get("element"));
        return ResponseEntity.ok(getElements(iterator));
    }

    /**
     * Istanziazione di un ConcreteIterator per e ricette in base all'ingredient<br/>
     * Vengono cercate tutte le ricette che contengono un certo ingredient
     * @return un set di ricette in formato Lazy
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByIngredient
     */
    public ResponseEntity<?> searchByIngredient(){
        Set<LazyResource> lazySet= decodeSet(this.body.get("res"));
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection()
                .createConcreteIteratorByIngredient(lazySet,(String) this.body.get("element"));
        return ResponseEntity.ok(getElements(iterator));
    }

    /**
     * Istanziazione di un ConcreteIterator per e ricette in base al tempo di esecuzione<br/>
     * Vengono cercate tutte le ricette che hanno come tempo di esecuzione un lavore inferiore a quello inserito dal client
     * @return un set di ricette in formato Lazy
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByTime
     */
    public ResponseEntity<?> searchByExecutionTime(){
        Set<LazyResource> lazySet= decodeSet(this.body.get("res"));
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection()
                .createConcreteIteratorByExecutionTime(lazySet,(int) this.body.get("element"));
        return ResponseEntity.ok(getElements(iterator));
    }

    /**
     * Istanziazione di un ConcreteIterator per e ricette in base alla difficoltò<br/>
     * Vengono cercate tutte le ricette che hanno una diffixoltà inferiore a quella inserita dal client
     * @return un set di ricette in formato Lazy
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByDifficult
     */
    public ResponseEntity<?> searchByDifficult(){
        Set<LazyResource> lazySet=decodeSet(this.body.get("res"));
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection()
                .createConcreteIteratorByExecutionTime(lazySet,(int) this.body.get("element"));
        return ResponseEntity.ok(getElements(iterator));
    }


    /**
     * Istanziazione di tutti i ConcreteIterator per e ricette<br/>
     * Vengono cercate tutte le ricette in ordine che:
     * <ul>
     * <li>contengono uno o piu nomi<li/>
     * <li>contengono uno o piu ingredienti<li/>
     * <li>tempo di esecuzione inferirore a quelli passati dal client<li/>
     * <ul/>
     * @return un set di ricette in formato Lazy
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByName
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByIngredient
     * @see net.studionotturno.backend_ICookbook.iterators.ConcreteIteratorByDifficult
     */

    public ResponseEntity<?> searchByTotalTags(){
        Set<LazyResource> lazySet=decodeSet(this.body.get("res"));//decodifica del json con le lazyres del clinet già presenti nella pagina della ricerca
        Document doc=Document.parse((String) this.body.get("element"));//decodifica del body del messaggio(contiene ttti i tag da usare)
        MyIterator iterator;
        Set<LazyResource> set1=new HashSet<>();
        //filtraggio per nome
        for (String value:((List<String>) doc.get("name"))) {
            iterator=  new ConcreteCollection().createConcreteIteratorByName(set1,value);
            set1.clear();    while(iterator.hasNext()) set1.add(iterator.next());
        }
        //filtraggio per ingrediente sullo stesso set ottenuto prima
        for (String value:((List<String>) doc.get("ing"))) {
            iterator=  new ConcreteCollection().createConcreteIteratorByIngredient(set1,value);
            set1.clear();   while(iterator.hasNext()) set1.add(iterator.next());
        }
        //filtraggio per tempo di esecuzione sullo stesso set ottenuto prima
        for (String value:((List<String>) doc.get("time"))) {
            iterator=  new ConcreteCollection().createConcreteIteratorByExecutionTime(set1,Integer.parseInt(value));
            set1.clear();    while(iterator.hasNext()) set1.add(iterator.next());
        }
        return ResponseEntity.ok(set1.stream().map((el->el.toJson())).collect(Collectors.toSet()));
    }

    /**
     * Consente di iterare gli elementi e riempire una struttura dati per la risposta verso il client
     * @param iterator l'iteratore impostato per la ricerca
     * @return il set dei risultati
     */
    private Set<Document> getElements(MyIterator iterator) {
        Set<Document> set=new HashSet<>();
        while(iterator.hasNext()) set.add(iterator.next().toJson());
        return set;
    }

    /**
     * Trasforma parte del body del messaggio proveniente dal client
     * in una struttura dati idonea per eseguire le query con mongodb
     * @param set la struttura dati proveniente dal client, le lazy resources di un eventuale set precedente
     * @return un set di LaxyResource codificata dal json in input
     */
    private Set<LazyResource> decodeSet(Object set) {
        Set<LazyResource> lazySet=new HashSet<>();
        ((List<Document>)set).forEach((el)-> lazySet.add(new LazyResource().toObject(el)) );
        return lazySet;
    }


}
