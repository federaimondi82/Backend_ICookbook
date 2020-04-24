package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;
import java.util.Set;
import static com.mongodb.client.model.Filters.*;

/**
 * Un ConcreteIterator del patter Itertor GOF; implementa i metodi dell'Iterator
 * e fornisce il set con gli elementi che hanno un ineiemente di ingredient passti come parametro
 */
public class ConcreteIteratorByIngredient implements MyIterator {

	Set<LazyResource> set;
	int currentPosition=0;

	@Override
	public LazyResource next(){
		if(hasNext()) return (LazyResource) this.set.toArray()[currentPosition++];
		return null;
	}
	@Override
	public boolean hasNext() {
		return this.currentPosition<set.size();
	}
	@Override
	public void reset() {
		this.currentPosition=0;
	}

	public ConcreteIteratorByIngredient(Set<LazyResource> set,String ingredinetName)
	{
		this.set=searchByIngredient(set,ingredinetName);
	}

	private Set<LazyResource> searchByIngredient(Set<LazyResource> set, String ingredientName) {
		return getResult(set,or(regex("ingredients.name",ingredientName), regex("ingredients.ingredients.name",ingredientName)));
	}
}