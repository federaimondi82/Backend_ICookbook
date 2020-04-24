package net.studionotturno.backend_ICookbook.controllers;

import net.studionotturno.backend_ICookbook.domain.JsonChecker;
import net.studionotturno.backend_ICookbook.domain.LazyResource;
import net.studionotturno.backend_ICookbook.iterators.ConcreteCollection;
import net.studionotturno.backend_ICookbook.iterators.MyIterator;
import org.bson.Document;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Classe con responsabilità di gestire le chiamate dal client relative alle ricerche delle ricette
 */
@RestController
public class SearcherController {

    /**
     * Un utente decide di fare una ricera di ricette sul backend in base al nome passato come parametro
     * @param json il corpo del messaggio proveniente dal client
     * @param name il nome o parte di un nome di una ricetta cercata dall'utente
     * @return un insieme di mongoDb Documents verso il client
     */
    @PostMapping(value="docu/iterator/byName/{name}")
    public Set<Document> searchByName(@RequestBody String json, @PathVariable String name){
        if(!JsonChecker.getInstance().checkJson(Document.parse(json))) return null;
        Set<LazyResource> lazyList=decodeBody(json);
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection().createConcreteIteratorByName(lazyList,name);
        return getElements(iterator);
    }

    /**
     * Un utente decide di fare una ricera di ricette sul backend in base al nome di un ingredient come parametro
     * @param json il corpo del messaggio proveniente dal client
     * @param name il nome o parte di un nome di un ingrediente cercata dall'utente
     * @return un insieme di mongoDb Documents verso il client
     */
    @PostMapping(value="docu/iterator/byIngredient/{name}")
    public Set<Document> searchByIngredient(@RequestBody String json, @PathVariable String name){
        if(!JsonChecker.getInstance().checkJson(Document.parse(json))) return null;
        Set<LazyResource> lazyList= decodeBody(json);
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection().createConcreteIteratorByIngredient(lazyList,name);

        return getElements(iterator);
    }

    /**
     * Un utente decide di fare una ricera di ricette sul backend in base al tempo di esecuzione
     * @param json il corpo del messaggio proveniente dal client
     * @param time il tempo di esecuzione della ricetta (in minuti) cercata dall'utente
     * @return un insieme di mongoDb Documents verso il client
     */
    @PostMapping(value="docu/iterator/byExecutionTime/{time}")
    public Set<Document> searchByExecutionTime(@RequestBody String json, @PathVariable String time){
        if(!JsonChecker.getInstance().checkJson(Document.parse(json))) return null;
        Set<LazyResource> lazyList=decodeBody(json);
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection().createConcreteIteratorByExecutionTime(lazyList,Integer.parseInt(time));
        return getElements(iterator);
    }

    /**
     * Un utente decide di fare una ricera di ricette sul backend in base alla difficoltà
     * @param json il corpo del messaggio proveniente dal client
     * @param difficult la difficoltà di esecuzione della ricetta (da 1 a 10) cercata dall'utente
     * @return un insieme di mongoDb Documents verso il client
     */
    @PostMapping(value="docu/iterator/byDifficult/{difficult}")
    public Set<Document> searchByDifficult(@RequestBody String json, @PathVariable String difficult){
        if(!JsonChecker.getInstance().checkJson(Document.parse(json))) return null;
        Set<LazyResource> lazyList=decodeBody(json);
        //istanziazione dell'iterator
        MyIterator iterator=  new ConcreteCollection().createConcreteIteratorByExecutionTime(lazyList,Integer.parseInt(difficult));

        return getElements(iterator);
    }

    /**
     * TOTALE
     * */
    @PostMapping(value="docu/iterator/byTotalTags/{totalElements}")
    public Set<Document> searchByTotalTags(@RequestBody String json, @PathVariable String totalElements){
        if(!JsonChecker.getInstance().checkJson(Document.parse(json))) return null;
        Set<LazyResource> lazyList=decodeBody(json);//decodifica del json con le lazyres del clinet già presenti nella pagina della ricerca
        Document doc=Document.parse(totalElements);//decodifica del body del messaggio(contiene ttti i tag da usare)
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
        return set1.stream().map((el->el.toJson())).collect(Collectors.toSet());
    }


    /**
     * COnsente di iterara gli elementi e riempire una lista per la risposta verso il client
     * @param iterator l'iteratore impostato per la ricerca
     * @return la lista dei risultati
     */
    private Set<Document> getElements(MyIterator iterator) {
        Set<Document> set=new HashSet<>();
        while(iterator.hasNext()){
            //ogni lazyResource viene trasformata in Document per l'invio al client
            set.add(iterator.next().toJson());
        }
        return set;
    }

    /**
     * Consente di decodificare il corpo del messaggio proveniente dal client<br/>
     * in una struttura dati idonea per eseguire le query con mongodb
     * @param json la struttura dati proveniente dal client
     * @return un set di LaxyResource codificata dal json in input
     */
    private Set<LazyResource> decodeBody(String json) {
        Set<LazyResource> lazyList=new HashSet<LazyResource>();
        //decodifica del messaggio in una lista di Document
        Document d=Document.parse(json);
        //estrapola il set dal document e riempie il Set di LazyResource
        List<Document> list1= (List<Document>) d.get("res");
        list1.forEach(el->lazyList.add(new LazyResource().toObject(el)));
        return lazyList;
    }
}
