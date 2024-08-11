# src/main/python/validator.py

import traceback

def validate_and_execute(user_code, input_array):
    try:
        # Define the environment where the user code will run
        local_env = {}
        
        # Execute the user's code
        exec(user_code, {}, local_env)
        
        # Check if the 'sort' function is defined
        if 'sort' not in local_env:
            return {'error': 'Function "sort" is not defined.'}
        
        # Check if 'sort' is callable and has exactly one parameter
        sort_func = local_env['sort']
        if not callable(sort_func):
            return {'error': '"sort" is not callable.'}
        if sort_func.__code__.co_argcount != 1:
            return {'error': '"sort" must have exactly one parameter named "arr".'}
        
        # Call the 'sort' function with the input array
        steps = sort_func(input_array)
        
        # Check if the return value is an array of arrays of integers
        if not isinstance(steps, list):
            return {'error': '"sort" must return a list of lists of integers.'}
        
        # Convert steps to ensure they are lists of integers
        converted_steps = []
        for step in steps:
            #if not isinstance(step, list):
            #    return {'error': 'Each element of the "steps" list must be a list of integers.'}
            converted_step = [int(i) for i in step]
            converted_steps.append(converted_step)
        
        # Return the steps if everything is correct
        return {'steps': converted_steps}
    
    except Exception as e:
        # Return any exception tracebacks
        return {'error': str(e), 'traceback': traceback.format_exc()}
