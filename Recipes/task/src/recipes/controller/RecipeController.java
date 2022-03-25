package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.entity.User;
import recipes.service.RecipeService;
import recipes.service.UserDetailsServiceImpl;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    RecipeService service;
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    RecipeController(RecipeService service, UserDetailsServiceImpl userDetailsService) {
        this.service = service;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipes(@PathVariable("id") Long id) {
        if (service.getRecipeById(id).isPresent()) {
            return new ResponseEntity<>(service.getRecipeById(id).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> postRecipes(@AuthenticationPrincipal User user
            , @Valid @RequestBody Recipe recipe) {
        recipe.setEmail(user.getUsername());
        Recipe newRecipe = service.saveRecipe(recipe);
        return new ResponseEntity<>(Map.of("id", newRecipe.getId()), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> removeRecipe(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        Optional<Recipe> optional = service.getRecipeById(id);
        if (optional.isPresent()) {
            if (optional.get().getEmail().equals(user.getUsername())) {
                service.deleteRecipe(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable("id") Long id
            , @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal User user) {
        Optional<Recipe> optional = service.getRecipeById(id);
        if (optional.isPresent()) {
            if (optional.get().getEmail().equals(user.getUsername())) {
                recipe.setId(id);
                recipe.setEmail(user.getUsername());
                service.saveRecipe(recipe);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipeBy(@RequestParam(value = "category", required = false) String category
            , @RequestParam(value = "name", required = false) String name) {
        List<Recipe> list;
        if(category != null && name == null) {
            list = service.findAllByCategory(category);
        } else if (name != null && category == null) {
            list = service.findAllByName(name);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
