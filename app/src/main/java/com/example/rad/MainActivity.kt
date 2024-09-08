package com.example.rad

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.rad.algorithm.Algorithm
import com.example.rad.algorithm.AlgorithmEvents
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.algorithm.AlgorithmViewModelProvider
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.chatgpt.OPENAI_API_KEY
import com.example.rad.complexity.AlgorithmComponent
import com.example.rad.complexity.AlgorithmInfo
import com.example.rad.complexity.parseAlgorithmInfoJson
import com.example.rad.components.PythonComponent
import com.example.rad.components.VisBottomBar
import com.example.rad.components.VisComponent
import com.example.rad.components.VisMain
import com.example.rad.database.DatabaseComponent
import com.example.rad.database.DatabaseViewModel
import com.example.rad.database.InformationAndQuizScreen
import com.example.rad.database.InsertOrChangeAlgorithm
import com.example.rad.database.PreviewOrDeleteAlgorithm
import com.example.rad.database.VisualizerScreen
// import com.example.rad.highlighting.SyntaxComponent
import com.example.rad.highlighting.WebComponent
import com.example.rad.quiz.Answer
import com.example.rad.quiz.Question
import com.example.rad.quiz.QuizComponent
import com.example.rad.quiz.parseQuizJson
import com.example.rad.similarity.AlgorithmComparison
import com.example.rad.similarity.ComparisonComponent
import com.example.rad.similarity.ImprovementSuggestions
import com.example.rad.similarity.parseAlgorithmComparisonsJson
import com.example.rad.ui.theme.RadTheme
import io.github.cdimascio.dotenv.Configuration
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

@Composable
fun MainApp(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel,
    chatViewModel: ChatViewModel
) {
    var isDarkTheme by remember { mutableStateOf(false) }

    RadTheme(darkTheme = isDarkTheme) {
        MainScreen(
            databaseViewModel = databaseViewModel,
            viewModel = viewModel,
            chatViewModel = chatViewModel,
            isDarkTheme = isDarkTheme,
            onThemeToggle = { isDarkTheme = !isDarkTheme }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel,
    chatViewModel: ChatViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Switcher and Navigation") },
                actions = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeToggle() }
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(it)
        ) {
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("insertOrChange") {
                InsertOrChangeAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("previewOrDelete") {
                PreviewOrDeleteAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("visualizer") {
                VisualizerScreen(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("informationAndQuiz") {
                InformationAndQuizScreen(
                    databaseViewModel = databaseViewModel,
                    chatViewModel = chatViewModel,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { navController.navigate("insertOrChange") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Insert or Change Algorithm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("previewOrDelete") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Preview or Delete Algorithm")
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Main Screen") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Visualizer") },
            label = { Text("Visualizer") },
            selected = navController.currentDestination?.route == "visualizer",
            onClick = {
                navController.navigate("visualizer") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Information and Quiz") },
            label = { Text("Info & Quiz") },
            selected = navController.currentDestination?.route == "informationAndQuiz",
            onClick = {
                navController.navigate("informationAndQuiz") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }
}


class MainActivity : ComponentActivity() {
    private val viewModel: AlgorithmViewModel by lazy {
        val viewModelProviderFactory = AlgorithmViewModelProvider(Algorithm())
        ViewModelProvider(this, viewModelProviderFactory)[AlgorithmViewModel::class.java]
    }

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val databaseViewModel: DatabaseViewModel by lazy {
        ViewModelProvider(this)[DatabaseViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val dotenv = dotenv {
            directory = "./assets"
            filename = "env"
        }
        OPENAI_API_KEY = dotenv["OPENAI_API_KEY"]

        setContent {
            // this.deleteDatabase("algorithm_database")

            // Visualization component, syntax highlighter, Python code input
            // Once the user inputs proper Python code (in desired format), it's ready for visualization
            // Syntax highlighter is currently of no use, but idea is following
            // I want to input the Python code through this syntax highlighter, and not normal text area
            // This should be somehow integrated with the Python code input
            /*
            var isDarkTheme by remember { mutableStateOf(false) }
            RadTheme(darkTheme = isDarkTheme) {
                MainScreen(viewModel = viewModel, isDarkTheme = isDarkTheme, onThemeToggle = { isDarkTheme = !isDarkTheme })
            }
            */

            // This part was used to test ChatGPT API calls, currently not of use
            /*
            val userCodeStatic = """
            def sort(arr):
                steps = []
                for i in range(1, len(arr)):
                    j = i - 1
                    key = arr[i]
                    while j >= 0 and key < arr[j]:
                        arr[j+1] = arr[j]
                        steps.append(arr[:])
                        j -= 1
                    arr[j+1] = key
                    steps.append(arr[:])
                return steps
            """.trimIndent()

            val gResponse by chatViewModel.gptResponse.observeAsState(initial = "")
            val gError by chatViewModel.gptError.observeAsState(initial = "")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    if (gResponse.isNotEmpty()) {
                        Text(text = gResponse)
                    } else if (gError.isNotEmpty()) {
                        Text(text = gError, color = Color.Red)
                    }

                    Button(onClick = { chatViewModel.createChatCompletion(userCodeStatic, 0) }) {
                        Text(text = "Quiz")
                    }
                    Button(onClick = { chatViewModel.createChatCompletion(userCodeStatic, 1) }) {
                        Text(text = "Complexity")
                    }
                    Button(onClick = { chatViewModel.createChatCompletion(userCodeStatic, 2) }) {
                        Text(text = "Similarity")
                    }
                }
            }
            */

            // When user clicks the button, ChatGPT should return quiz in specific format
            // This component is used to represent that quiz in a proper way
            /*
            val input = """
                [
                    {
                        "question": "What sorting algorithm is implemented in the provided code?",
                        "answers": [
                            { "0": "Bubble Sort" },
                            { "1": "Selection Sort" },
                            { "2": "Insertion Sort" },
                            { "3": "Merge Sort" }
                        ],
                        "correct_answers": [2]
                    },
                    {
                        "question": "What is the best-case time complexity of this algorithm?",
                        "answers": [
                            { "0": "O(n)" },
                            { "1": "O(n log n)" },
                            { "2": "O(n^2)" },
                            { "3": "O(log n)" }
                        ],
                        "correct_answers": [0]
                    },
                    {
                        "question": "What is the worst-case time complexity of this algorithm?",
                        "answers": [
                            { "0": "O(n)" },
                            { "1": "O(n log n)" },
                            { "2": "O(n^2)" },
                            { "3": "O(n^3)" }
                        ],
                        "correct_answers": [2]
                    },
                    {
                        "question": "Which of the following statements is true about this algorithm?",
                        "answers": [
                            { "0": "It uses a divide-and-conquer strategy" },
                            { "1": "It is stable" },
                            { "2": "It uses a fixed number of iterations for all inputs" },
                            { "3": "It has a space complexity of O(n)" }
                        ],
                        "correct_answers": [1]
                    },
                    {
                        "question": "How does the implementation of this algorithm keep track of its progress?",
                        "answers": [
                            { "0": "By using a 'steps' list to record the array's state after each key operation" },
                            { "1": "By using recursion" },
                            { "2": "By implementing nested loops only" },
                            { "3": "By employing extra space proportional to the size of the input array" }
                        ],
                        "correct_answers": [0]
                    },
                    {
                        "question": "Can the given algorithm be improved in terms of time complexity?",
                        "answers": [
                            { "0": "No, this is the most optimized version" },
                            { "1": "Yes, by using a different sorting algorithm like Quick Sort or Merge Sort" },
                            { "2": "No, the inherent complexity of insertion sort cannot be improved" },
                            { "3": "Yes, by using a different sorting algorithm like Bubble Sort or Selection Sort" }
                        ],
                        "correct_answers": [1, 3]
                    }
                ]
            """
            val quiz = parseQuizJson(input)
            QuizComponent(quiz = quiz)
            */

            // When user clicks the button, ChatGPT should return some details about the algorithm
            // This component is used to represent those details in a proper way
            /*
            val input = """
                {
                    "intro": "The provided function is an implementation of the Insertion Sort algorithm which stores each step of the sorting process in a list `steps`.",
                    "time_complexity": "The time complexity of the Insertion Sort algorithm is as follows:\n- Best Case: O(n) when the array is already sorted.\n- Average Case: O(n^2) as each insertion operation involves scanning and potentially shifting elements.\n- Worst Case: O(n^2) when the array is sorted in reverse order.\nThe nested loops are indicative of the quadratic time complexity in the average and worst cases.",
                    "space_complexity": "The space complexity is O(n) for the auxiliary storage `steps`, which grows proportionally to the number of elements in the array.\nThe algorithm itself is in-place with O(1) additional space for sorting, but `steps` array significantly increases space usage.",
                    "insights": "This implementation is efficient for small datasets due to its simple in-place mechanism but becomes inefficient for larger datasets due to its quadratic time complexity.\nThe `steps` list captures each state of the array during sorting, which can be useful for educational purposes or debugging but increases memory usage significantly.",
                    "conclusion": "While the temporal complexity can be acceptable for small arrays or nearly sorted datasets, the overall space complexity is elevated due to the storage of interim steps.\nThe function is beneficial in learning or tracing the sorting process but may not be practical for sorting large datasets due to the additional memory requirements needed for the `steps` list."
                }
            """
            val algorithmInfo = parseAlgorithmInfoJson(input)
            AlgorithmComponent(algorithmInfo = algorithmInfo)
            */

            // When user clicks the button, ChatGPT should return some comparison with common known algorithms
            // This component is used to represent that comparison in a proper way
            /*
            val input = """
                [
                    {
                        "algorithm_name": "Insertion Sort",
                        "efficiency_time_comparison": "Both the provided algorithm and insertion sort have a worst-case time complexity of O(n^2) and a best-case of O(n) when the data is already sorted.",
                        "efficiency_space_comparison": "Both use O(1) of extra space, making them in-place algorithms. The provided algorithm also stores steps, which increases space complexity to O(n^2) in the worst case.",
                        "implementation_comparison": "The provided code closely follows the standard insertion sort with an added feature of tracking and storing intermediate steps.",
                        "use_cases_comparison": "Insertion sort and the provided algorithm are both well-suited for small or nearly sorted datasets. The additional steps feature in the provided algorithm is useful for educational purposes.",
                        "conclusion": "The provided sorting method is an insertion sort with a modification to track each step of the algorithm, making it more suitable for learning and visualization purposes."
                    },
                    {
                        "algorithm_name": "Bubble Sort",
                        "efficiency_time_comparison": "Both have a worst-case time complexity of O(n^2). However, in practice, insertion sort usually performs better as elements are likely to be more uniformly displaced from their correct sorted position.",
                        "efficiency_space_comparison": "Both are in-place and use O(1) additional space. The provided algorithm, when storing steps, uses more space for the list of steps.",
                        "implementation_comparison": "Bubble sort repeatedly swaps adjacent elements until the list is sorted, while insertion sort builds the sorted array one element at a time. The provided code's logic is closer to insertion sort than bubble sort.",
                        "use_cases_comparison": "Bubble sort is rarely used in practical applications due to its inefficiency, except for educational demonstrations. Insertion sort, including the provided version, is more practical for small datasets or nearly sorted data.",
                        "conclusion": "The provided algorithm is more efficient and practical than bubble sort in most cases. Its step-tracking feature is also beneficial for educational purposes."
                    },
                    {
                        "improvement_suggestions": [
                            "Consider adding a break condition if no elements are swapped in a pass to potentially improve performance.",
                            "You might want to consider optimizing the step tracking if space becomes an issue, possibly by using a generator or logging steps selectively.",
                            "Adding type hints and comments can improve code readability and maintainability."
                        ],
                        "conclusion": "The algorithm provided is well-suited for educational purposes or small datasets. By applying the suggested improvements, you can enhance its performance and usability. For larger datasets, considering more efficient algorithms like merge sort or quicksort would be beneficial."
                    }
                ]
            """
            val (algorithmComparisons, improvementSuggestions) = parseAlgorithmComparisonsJson(input)
            ComparisonComponent(
                algorithmComparisons = algorithmComparisons,
                improvementSuggestions = improvementSuggestions!!
            )
            */

            // This should be integrated with the Python code input (and syntax highlighter, that should be integrated with the Python code input)
            // Once the user inputs the Python code in desired format, by using this component, it should be added to the database
            // It also has option for algorithm display, and its deletion
            // DatabaseComponent(databaseViewModel = databaseViewModel, viewModel = viewModel)
            // InsertOrChangeAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)
            // PreviewOrDeleteAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)

            // MainScreen(databaseViewModel = databaseViewModel, viewModel = viewModel, chatViewModel = chatViewModel)
            MainApp(
                databaseViewModel = databaseViewModel,
                viewModel = viewModel,
                chatViewModel = chatViewModel
            )

            /*
            InformationAndQuizScreen(
                databaseViewModel = databaseViewModel,
                chatViewModel = chatViewModel,
                viewModel = viewModel
            )
            */
        }
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: AlgorithmViewModel, isDarkTheme: Boolean, onThemeToggle: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Switcher and Navigation") },
                actions = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeToggle() }
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "vis", modifier = Modifier.padding(innerPadding)) {
            composable("vis") { VisComponent(viewModel = viewModel) }
            // composable("syntax") { SyntaxComponent() }
            // composable("python") { PythonComponent(viewModel = viewModel) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Visibility, contentDescription = "Visualize") },
            label = { Text("Visualize") },
            selected = navController.currentBackStackEntry?.destination?.route == "vis",
            onClick = { navController.navigate("vis") }
        )
        /*
        NavigationBarItem(
            icon = { Icon(Icons.Default.Code, contentDescription = "Syntax") },
            label = { Text("Syntax") },
            selected = navController.currentBackStackEntry?.destination?.route == "syntax",
            onClick = { navController.navigate("syntax") }
        )
        */
        NavigationBarItem(
            icon = { Icon(Icons.Default.PanTool, contentDescription = "Python") },
            label = { Text("Python") },
            selected = navController.currentBackStackEntry?.destination?.route == "python",
            onClick = { navController.navigate("python") }
        )
    }
}
*/
