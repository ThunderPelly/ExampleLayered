import matplotlib.pyplot as plt
import pandas as pd
import numpy as np

# Data for ExampleLayered
layered_steps = ['User: Value Object', 'Task: Value Object', 'Project: Value Object']
layered_edited_files_over_all = [13, 25, 12]
layered_edited_tests = [18, 20, 9]
layered_cyclomatic_complexity = [44, 46, 48]
layered_cognitive_complexity = [10, 10, 10]
layered_technical_dept = [5, 5, 5]
layered_test_coverage = [93.3, 90.1, 86.8]
layered_loc = [524, 537, 597]

# Data for ExampleHexagonal
hexagonal_steps = ['User: Value Object', 'Task: Value Object', 'Project: Value Object']
hexagonal_edited_files_over_all = [3, 5, 3]
hexagonal_edited_tests = [0, 0, 0]
hexagonal_cyclomatic_complexity = [57, 59, 60]
hexagonal_cognitive_complexity = [12, 12, 12]
hexagonal_technical_dept = [7, 7, 7]
hexagonal_test_coverage = [93.7, 90.6, 90.3]
hexagonal_loc = [730, 756, 763]

# Convert data to Pandas DataFrame
layered_data = {
    'Step': layered_steps,
    'Edited Files': layered_edited_files_over_all,
    'Edited Test Case': layered_edited_tests,
    'Cyclomatic Complexity': layered_cyclomatic_complexity,
    'Cognitive Complexity': layered_cognitive_complexity,
    'Technical Dept': layered_technical_dept,
    'Test Coverage': layered_test_coverage,
    'LOC': layered_loc,
}

hexagonal_data = {
    'Step': hexagonal_steps,
    'Edited Files': hexagonal_edited_files_over_all,
    'Edited Test Case': hexagonal_edited_tests,
    'Cyclomatic Complexity': hexagonal_cyclomatic_complexity,
    'Cognitive Complexity': hexagonal_cognitive_complexity,
    'Technical Dept': hexagonal_technical_dept,
    'Test Coverage': hexagonal_test_coverage,
    'LOC': hexagonal_loc,
}

# Add 'Project' column
layered_df = pd.DataFrame(layered_data)
layered_df['Project'] = 'ExampleLayered'

hexagonal_df = pd.DataFrame(hexagonal_data)
hexagonal_df['Project'] = 'ExampleHexagonal'

# Combine both projects in one DataFrame
combined_df = pd.concat([layered_df, hexagonal_df])

# Define the order of steps
step_order = ['User: Value Object', 'Task: Value Object', 'Project: Value Object']
combined_df['Step'] = pd.Categorical(combined_df['Step'], categories=step_order, ordered=True)

# Define axis labels
x_axis_label = 'Development Steps'
y_axis_label = {
    'Edited Files': 'Number of Edited Files',
    'Edited Test Case': 'Number of Edited Test Cases',
    'Cyclomatic Complexity': 'Cyclomatic Complexity',
    'Cognitive Complexity': 'Cognitive Complexity',
    'Technical Dept': 'Technical Debt',
    'Test Coverage': 'Test Coverage (%)',
    'LOC': 'Lines of Code'
}

# Create a plot with all subplots together
fig, axes = plt.subplots(nrows=2, ncols=3, figsize=(15, 8))

# Plot each metric
for i, (metric, plot_type) in enumerate([('Edited Files', 'bar'), ('Edited Test Case', 'bar'),
                                          (['Cyclomatic Complexity', 'Cognitive Complexity'], 'bar'),
                                          ('Technical Dept', 'bar'), ('Test Coverage', 'line'), ('LOC', 'line')]):
    row = i // 3
    col = i % 3

    ax = axes[row, col]

    if isinstance(metric, list):
        # Handle the case when the metric is a list
        combined_df.pivot_table(index='Step', columns='Project', values=metric).plot(kind=plot_type, ax=ax)
    else:
        combined_df.pivot_table(index='Step', columns='Project', values=metric).plot(kind=plot_type, ax=ax)
        ax.set_title(y_axis_label[metric])

    ax.set_xlabel(x_axis_label)
    ax.legend(title='Project')

# Adjust layout and save the combined plot
plt.tight_layout()
plt.savefig('combined_plots.png')

# Add a title and show the combined plot
plt.suptitle('Development Metrics Across Steps')
plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()
