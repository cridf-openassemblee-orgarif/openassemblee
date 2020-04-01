import HttpService, { SecurityCsrfToken } from './HttpService';
import EventBus from './EventBus';

class Injector {
    public static instance: Injector;

    public urlBase = global.urlBase ? global.urlBase : '';

    public httpService = new HttpService();

    applicationEventBus = new EventBus<ApplicationEvents>();
}

export const injector = () => {
    if (!Injector.instance) {
        Injector.instance = new Injector();
    }
    return Injector.instance;
};
