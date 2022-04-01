import 'webpack-entry';

import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';

import ZoomNotificationForm from 'form/ZoomNotificationForm';
import ZoomNotificationSummary from 'form/ZoomNotificationSummary';
import ZoomNotificationDetails from 'form/ZoomNotificationDetails';

import packageJson from '../../package.json';

const manifest = new PluginManifest(packageJson, {
    eventNotificationTypes: [
    {
      type: 'zoom-notification-v1',
      displayName: 'Zoom Notification',
      formComponent: ZoomNotificationForm,
      summaryComponent: ZoomNotificationSummary,
      detailsComponent: ZoomNotificationDetails,
      defaultConfig: ZoomNotificationForm.defaultConfig
    }
  ],
  /* This is the place where you define which entities you are providing to the web interface.
     Right now you can add routes and navigation elements to it.
     Examples: */
  // Adding a route to /sample, rendering YourReactComponent when called:
  // routes: [
  //  { path: '/sample', component: YourReactComponent, permissions: 'inputs:create' },
  // ],
  // Adding an element to the top navigation pointing to /sample named "Sample":
  // navigation: [
  //   { path: '/sample', description: 'Sample' },
  // ]
});

PluginStore.register(manifest);
