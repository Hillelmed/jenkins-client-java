package io.github.hmedioni.jenkins.client.binders;


public class BindMapToForm {

//   @Override
//   public <R extends HttpRequest> R bindToRequest(final R request, final Object properties) {
//
//       if (properties == null) {
//           return (R) request.toBuilder().build();
//       }
//
//      checkArgument(properties instanceof Map, "binder is only valid for Map");
//      Map<String, List<String>> props = (Map<String, List<String>>) properties;
//
//      Builder<?> builder = request.toBuilder();
//      for (Map.Entry<String, List<String>> prop : props.entrySet()) {
//         if (prop.getKey() != null) {
//            String potentialKey = prop.getKey().trim();
//            if (potentialKey.length() > 0) {
//                if (prop.getValues() == null) {
//                    prop.setValue(List.of(""));
//                }
//
//                builder.addFormParam(potentialKey, prop.getValues().toArray(new String[prop.getValues().size()]));
//            }
//         }
//      }
//
//      return (R) builder.build();
//   }
}
