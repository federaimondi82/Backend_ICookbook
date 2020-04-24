package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;

import java.util.Set;

import static com.mongodb.client.model.Filters.*;

/**
 * Un ConcreteIterator del patter Itertor GOF; implementa i metodi dell'Iterator
 * e fornisce il set con gli elementi che hanno un tepo di esecuzione peggiore di quello passato come parametro
 */
public class ConcreteIteratorByTime implements MyIterator {
	Set<LazyResource> set;
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

	public ConcreteIteratorByTime(Set<LazyResource> set,int time) {
		this.set=searchByTime(set,time);
	}

	private Set<LazyResource> searchByTime(Set<LazyResource> set, int time) {
		return getResult(set,lte("executionTime",time));
	}
}