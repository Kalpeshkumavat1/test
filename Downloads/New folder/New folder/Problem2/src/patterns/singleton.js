// Helper for enforcing singleton via class-level instance handling

function asSingleton(ClassConstructor) {
  let instance = null;
  return new Proxy(ClassConstructor, {
    construct(target, args) {
      if (!instance) {
        instance = Reflect.construct(target, args);
      }
      return instance;
    }
  });
}

module.exports = { asSingleton };


