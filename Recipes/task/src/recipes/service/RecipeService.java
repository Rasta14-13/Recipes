package recipes.service;

import recipes.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    Optional<Recipe> getRecipeById(Long id);

    Recipe saveRecipe(Recipe recipe);

    void deleteRecipe(Long id);

    List<Recipe> findAllByCategory(String category);

    List<Recipe> findAllByName(String name);
}
