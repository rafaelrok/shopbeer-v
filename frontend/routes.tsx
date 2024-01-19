import { createBrowserRouter, RouteObject } from 'react-router-dom';
// @ts-ignore
import {serverSideRoutes} from "./Flow";
// import {serverSideRoutes} from "Frontend/generated/flow/Flow";

export const routes = [
  ...serverSideRoutes
] as RouteObject[];

export default createBrowserRouter(routes);
