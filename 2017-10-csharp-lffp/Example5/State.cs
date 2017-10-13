using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Example5
{
    public class State<TState, TValue>
    {
        private readonly Func<TState, Tuple<TState, TValue>> _stateFunc;

        public State(Func<TState, Tuple<TState, TValue>> state)
        {
            _stateFunc = state;
        }

        public Tuple<TState, TValue> Run(TState initialState)
        {
            return _stateFunc(initialState);
        }

        public TValue Eval(TState initialState)
        {
            return Run(initialState).Item2;
        }
    }
}
