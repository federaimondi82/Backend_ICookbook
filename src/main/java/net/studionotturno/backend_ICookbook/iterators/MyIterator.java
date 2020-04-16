package net.studionotturno.backend_ICookbook.iterators;

import com.mongodb.client.FindIterable;
import net.studionotturno.backend_ICookbook.DbConnection.MongoDBConnection;
import net.studionotturno.backend_ICookbook.domain.LazyResource;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashSet;
import java.util.Set;

public interface MyIterator {

	public LazyResource next();

	public boolean hasNext();

	public void reset();

	/**
	 * @param set l'eventuale set di elementi precedentemente trovati proveniente dal client
	 * @param bson la query Bson da utilizzare
	 * @return
	 */
	default Set<LazyResource> getResult(Set<LazyResource> set, Bson bson){
		Set<Document> list=new HashSet<>();
		//query sul database di tutte le ricette con le parola indicata
		FindIterable<Document> f= MongoDBConnection.getInstance().setCollection("recipes").getDocumentQuery(bson);
		list = f.into(new HashSet<>());
		//list.forEach((el)-> System.out.println(el.toString()));
		if(!set.isEmpty()){//filtraggio dei risultati
			list=resultFiltering(set,list);
			//list.forEach((el)-> System.out.println(el.toString()));
		}

		return adaptaResult(list);
	}

	static Set<LazyResource> adaptaResult(Set<Document> list){
		Set<LazyResource> newSet=new HashSet<>();
		list.forEach((el)->{
			newSet.add(
					new LazyResource().setDocumentID(el.get("_id").toString())
							.setRecipeName(el.get("recipeName").toString())
							.setExecutionTime(Double.parseDouble(el.get("executionTime").toString()))
			);
		});
		//newSet.forEach((el)-> System.out.println(el.toString()));
		return newSet;
	}

	static Set<Document> resultFiltering(Set<LazyResource> set, Set<Document> list){
		Set<Document> filteredList=new HashSet<>();
		for (Document doc:list) {
			boolean trovato=false;
			for (LazyResource lazy:set ) if(doc.get("_id").toString().equals(lazy.getDocumentID()))trovato=true;
			if(trovato==true) filteredList.add(doc);
		}
		list.clear();  list.addAll(filteredList);
		return list;
	}
}