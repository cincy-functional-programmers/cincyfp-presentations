require "./lazy_stream"

module LazyStream
  class Node
    def show( *limit_and_options )
      options = {
        :sep => " ",
        :end => "\n",
      }.merge!(limit_and_options.last.is_a?(Hash) ? limit_and_options.pop : Hash.new)
      limit = limit_and_options.shift

      while head && (limit.nil? || (limit -= 1) > -1)
        print drop, options[:sep]
      end
      print options[:end]
    end
    alias_method :display, :show
  end
end
