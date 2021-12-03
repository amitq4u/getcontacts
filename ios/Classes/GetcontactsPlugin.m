#import "GetcontactsPlugin.h"
#if __has_include(<getcontacts/getcontacts-Swift.h>)
#import <getcontacts/getcontacts-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "getcontacts-Swift.h"
#endif

@implementation GetcontactsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftGetcontactsPlugin registerWithRegistrar:registrar];
}
@end
