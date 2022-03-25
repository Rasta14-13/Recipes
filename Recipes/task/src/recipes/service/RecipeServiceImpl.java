package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.entity.Recipe;
import recipes.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    RecipeRepository repository;

    @Autowired
    RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.repository = recipeRepository;
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return repository.save(recipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Recipe> findAllByCategory(String category) {
        return repository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    @Override
    public List<Recipe> findAllByName(String name) {
        return repository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name);
    }
}
