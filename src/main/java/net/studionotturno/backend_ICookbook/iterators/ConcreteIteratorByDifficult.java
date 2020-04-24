package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;
import java.util.Set;
import static com.mongodb.client.model.Filters.lte;


/**
 * Un ConcreteIterator del patter Itertor GOF; implementa i metodi dell'Iterator
 * e fornisce il set con gli elementi di una determineta difficoltà passata come paramento
 */
public class ConcreteIteratorByDifficult implements MyIterator {

	Set<LazyResource> set;
	int difficult;
	int currentPosition=0;
	@Override
	public LazyResource next() {
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

	public ConcreteIteratorByDifficult(Set<LazyResource> set,int difficult)
	{
		this.set=searchByDifficult(set,difficult);
	}

	private Set<LazyResource> searchByDifficult(Set<LazyResource> set, int difficult) {
		return getResult(set,lte("difficult",(double)difficult));
	}
}