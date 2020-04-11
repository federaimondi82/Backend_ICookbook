package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;

import java.util.Set;

public interface IterableCollection {

	public MyIterator createConcreteIteratorByName(Set<LazyResource> set, String name);

	public MyIterator createConcreteIteratorByIngredient(Set<LazyResource> set, String ingredientName);

	public MyIterator createConcreteIteratorByDifficult(Set<LazyResource> set, int difficult);

	public MyIterator createConcreteIteratorByExecutionTime(Set<LazyResource> set, int time);
}