package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;

import java.util.Set;

/**
 * Interfaccia del desin patter Iterator, mette a disposizione le operazione per la creazione di iteratori su una
 * colleione (una Concrete Collection)
 * */
public interface IterableCollection {

	public MyIterator createConcreteIteratorByName(Set<LazyResource> set, String name);

	public MyIterator createConcreteIteratorByIngredient(Set<LazyResource> set, String ingredientName);

	public MyIterator createConcreteIteratorByDifficult(Set<LazyResource> set, int difficult);

	public MyIterator createConcreteIteratorByExecutionTime(Set<LazyResource> set, int time);
}