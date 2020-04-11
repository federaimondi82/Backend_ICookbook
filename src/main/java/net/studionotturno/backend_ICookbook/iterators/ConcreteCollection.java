package net.studionotturno.backend_ICookbook.iterators;

import net.studionotturno.backend_ICookbook.domain.LazyResource;

import java.util.Set;

public class ConcreteCollection implements IterableCollection {

	@Override
	public MyIterator createConcreteIteratorByName(Set<LazyResource> set, String name) {
		return new ConcreteIteratorByName(set,name);
	}
	@Override
	public MyIterator createConcreteIteratorByIngredient(Set<LazyResource> set, String ingredientName) {
		return new ConcreteIteratorByIngredient(set,ingredientName);
	}
	@Override
	public MyIterator createConcreteIteratorByDifficult(Set<LazyResource> set, int difficult) {
		return new ConcreteIteratorByDifficult(set,difficult);
	}
	@Override
	public MyIterator createConcreteIteratorByExecutionTime(Set<LazyResource> set, int time) {
		return new ConcreteIteratorByTime(set,time);
	}
}