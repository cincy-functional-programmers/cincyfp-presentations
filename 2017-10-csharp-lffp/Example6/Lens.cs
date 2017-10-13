using Example5;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Example6
{
    public class Lens<TState, TValue>
    {
        public readonly Func<TState, TValue> Getter;
        public readonly Func<TState, TValue, TState> Setter;
          
        public Lens(Func<TState, TValue> getter, Func<TState, TValue, TState> setter)
        {
            Getter = getter;
            Setter = setter;
        }

        public Lens<TState, TOther> AndThen<TOther>(Lens<TValue, TOther> lens)
        {
            return new Lens<TState, TOther>(state => lens.Getter(Getter(state)), (s, o) => Setter(s, lens.Setter(Getter(s), o)));
        }  

        public State<TState, TValue> GetS() => Getter.Get();
        public State<TState, Unit> SetS(TValue value) => StateExtensions.Mod<TState>(s => Setter(s, value));        
    }
}
