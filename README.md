# annotation-processor-playground
This annotation processor provides code generation for the initialization of an `android.support.v7.app.AppCompatActivity` in a MVVM project.
To achieve that, it uses `android.databinding.ViewDataBinding`.
## Annotations:
### @AnnotatedActivity:
Contains the layoutId, this is the main entry point for the annotation processor.
### @DataBinding:
Attribute of the DataBinding, the class is generated by Google Data Binding when having the binding defined in the layout xml file.
### @ViewModel:
ViewModels that are defined in the DataBinding.
## Example:
The annotation processor can generate code for classes in the style of:
```java
@AnnotatedActivity(layout = R.layout.activity_main)
public class JMainActivity extends AppCompatActivity {
    @ViewModel(setter = "setViewModel")
    MainViewModel mainViewModel;

    @DataBinding
    ActivityMainBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        JMainActivityKt.setupViews(this);
    }
}
```
This class generates the following subclass:
```java
public class JMainActivityGen extends JMainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @LayoutRes int layout = 2130968602;

        setContentView(layout);
        binding = DataBindingUtil.setContentView(this, layout);
        setSupportActionBar(binding.toolbar);

        mainViewModel = new ch.domi.train_timetable.viewmodel.MainViewModel();
        binding.setViewModel(mainViewModel);
    }
}
```
The ViewModels must be defined in the DataBinding and a class can contain 0 to n ViewModels.
The annotation processor is capable of using only one DataBinding per Activity.