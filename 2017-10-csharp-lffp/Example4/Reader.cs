using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Example4
{
    public class Reader<TState, T>
    {
        private readonly Func<TState, T> _getter;

        public Reader(Func<TState, T> getter)
        {
            _getter = getter;
        }

        public T Run(TState state)
        {
            return _getter(state);
        }
    }
}
